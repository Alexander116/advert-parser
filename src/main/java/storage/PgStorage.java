package storage;


import api.*;
import api.interfaces.Settings;
import api.interfaces.Storage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * реализация хранилища через бд postgresql
 */
public class PgStorage implements Storage {

    private Connection connection;

    public PgStorage(Settings settings) throws AdParseException {
        this.connection = SingleDatabase.getInstance().getConnection(settings);
    }

    @Override
    public Advert findAdvertByUrl(Advert advert) throws AdParseException {
        Advert foundedAdvert = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM adverts WHERE project_id=? AND url=? LIMIT 1"
            );
            preparedStatement.setInt(1, advert.getProject().getId());
            preparedStatement.setString(2, advert.getUrl());
            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                foundedAdvert = resultSetToAdvert(results,advert);
            }
            preparedStatement.close();

        } catch (SQLException e) {
            throw new AdParseException(e);
        }

        return foundedAdvert;
    }


    @Override
    public void save(Advert advert) throws AdParseException {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO public.adverts( project_id, url, title, sum, \"desc\", date, img) VALUES (?, ?, ?, ?, ?, ?, ?);"
            );
            preparedStatement.setInt(1, advert.getProject().getId());
            preparedStatement.setString(2, advert.getUrl());
            preparedStatement.setString(3, advert.getTitle());
            preparedStatement.setFloat(4, advert.getSum());
            preparedStatement.setString(5, advert.getDesc());
            preparedStatement.setDate(6, new Date(advert.getDate()));
            preparedStatement.setString(7, advert.getImg());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new AdParseException(e);
        }
    }

    @Override
    public void update(Advert advert) throws AdParseException {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE adverts SET sum=?, date=?, title=?, url=?, \"desc\"=?, img=? WHERE id=?"
            );
            preparedStatement.setFloat(1, advert.getSum());
            preparedStatement.setLong(2, advert.getDate());
            preparedStatement.setString(3, advert.getTitle());
            preparedStatement.setString(4, advert.getUrl());
            preparedStatement.setString(5, advert.getDesc());
            preparedStatement.setString(6, advert.getImg());
            preparedStatement.setInt(7, advert.getId());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new AdParseException(e);
        }

    }


    @Override
    public Integer createProject(Project project) throws AdParseException {
        Integer id = null;
        try {
            Integer oldProject = findProjectByUrl(project.getUrl());
            if (oldProject == null) {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO projects( name, url) VALUES (?, ?);"
                );
                preparedStatement.setString(1, project.getName());
                preparedStatement.setString(2, project.getUrl());
                preparedStatement.executeUpdate();
                preparedStatement.close();
                id = findProjectByUrl(project.getUrl());
            }
        } catch (SQLException e) {
            throw new AdParseException(e);
        }
        return id;
    }

    @Override
    public List<Project> listAllProjects() throws AdParseException {
        List<Project> projects = new ArrayList<>();
        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM projects"
            );
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                projects.add(resultSetToProject(rs));
            }
            preparedStatement.close();
        } catch (Exception e) {
            throw new AdParseException(e);
        }
        return projects;
    }

    @Override
    public void deleteProjectWithAdverts(int id) throws AdParseException {
        try {
            deleteProjectAdverts(id);
            deleteProject(id);
        } catch (SQLException e) {
            throw new AdParseException(e);
        }
    }

    private Advert resultSetToAdvert(ResultSet rs, Advert advert) throws AdParseException {
        try{
            float sum = rs.getFloat("sum");
            String title = rs.getString("title");
            String img = rs.getString("img");
            int id = rs.getInt("id");
            long date = rs.getLong("date");
            String desc = rs.getString("desc");
            Project project = getProjectById(advert.getProject().getId());
            return new Advert(id, title, date, sum, advert.getUrl(), desc, img, project);
        } catch (SQLException e) {
            throw new AdParseException(e);
        }
    }

    private Project getProjectById(int projectId) throws AdParseException {
        Project project = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM projects WHERE id=?"
            );
            preparedStatement.setInt(1, projectId);
            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                project = resultSetToProject(results);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            throw new AdParseException(e);
        }

        return project;
    }

    /**
     * поиск проекта по ссылке
     *
     * @param url - ссылка
     * @return
     * @throws AdParseException
     */
    private Integer findProjectByUrl(String url) throws AdParseException {
        Integer id = null;
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM projects WHERE url = ? LIMIT 1;"
            );
            preparedStatement.setString(1, url);
            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                id = results.getInt("id");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            throw new AdParseException(e);
        }
        return id;
    }

    /**
     * инициализирует объект проекта из данных полученных от бд
     * @param results - данные полученные от бд
     * @return
     * @throws SQLException
     */
    private Project resultSetToProject(ResultSet results) throws SQLException {
        String name = results.getString("name");
        String url = results.getString("url");
        int id = results.getInt("id");
        boolean active = results.getBoolean("active");
        return new Project(id, name, url, active);
    }

    private void deleteProjectAdverts(int projectId) throws SQLException {

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM adverts WHERE project_id = ?"
            );
            preparedStatement.setInt(1, projectId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
    }

    private void deleteProject(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM projects WHERE id = ?"
        );
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }



}
