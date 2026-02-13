package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import domain.*;
import exceptions.NotFoundException;
import exceptions.ValidationException;
import service.GalleryService;
import util.ArtworkFactory;
import util.JsonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ArtGalleryHttpServer {

    private final GalleryService service;

    public ArtGalleryHttpServer(GalleryService service) {
        this.service = service;
    }

    public void start(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/artists", this::handleArtists);
        server.createContext("/artworks", this::handleArtworks);

        server.start();
        System.out.println("REST API started: http://localhost:" + port);
        System.out.println("Endpoints: GET/POST /artists | GET/POST /artworks | GET/PUT/DELETE /artworks/{id}");
    }

    private static void addCors(HttpExchange ex) {
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }

    private static boolean handleOptions(HttpExchange ex) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) {
            addCors(ex);
            ex.sendResponseHeaders(204, -1);
            return true;
        }
        return false;
    }

    private void handleArtists(HttpExchange ex) throws IOException {
        if (handleOptions(ex)) return;

        try {
            addCors(ex);
            String method = ex.getRequestMethod().toUpperCase();
            String path = ex.getRequestURI().getPath();

            if (path.matches("^/artists/?$")) {
                if ("GET".equals(method)) {
                    List<Artist> artists = service.listArtists();
                    writeJson(ex, 200, toJsonArtists(artists));
                    return;
                }

                if ("POST".equals(method)) {
                    String body = readBody(ex);

                    String name = JsonUtil.str(body, "name");
                    Integer yob = JsonUtil.intOrNull(body, "yearOfBirth");
                    String nat = JsonUtil.str(body, "nationality");

                    Artist artist = Artist.builder()
                            .name(name)
                            .yearOfBirth(yob == null ? 0 : yob)
                            .nationality(nat)
                            .build();

                    int id = service.createArtist(artist);
                    writeJson(ex, 200, "{\"id\":" + id + "}");
                    return;
                }

                writeJson(ex, 405, JsonUtil.err("Use GET/POST /artists"));
                return;
            }

            if (path.matches("^/artists/\\d+$")) {
                int id = Integer.parseInt(path.substring("/artists/".length()));
                if ("GET".equals(method)) {
                    Artist a = service.getArtist(id);
                    writeJson(ex, 200, toJsonArtist(a));
                    return;
                }
                writeJson(ex, 405, JsonUtil.err("Use GET /artists/{id}"));
                return;
            }

            writeJson(ex, 404, JsonUtil.err("Not found"));

        } catch (NotFoundException e) {
            writeJson(ex, 404, JsonUtil.err(e.getMessage()));
        } catch (ValidationException e) {
            writeJson(ex, 400, JsonUtil.err(e.getMessage()));
        } catch (Exception e) {
            writeJson(ex, 500, JsonUtil.err(e.getMessage()));
        }
    }

    private void handleArtworks(HttpExchange ex) throws IOException {
        if (handleOptions(ex)) return;

        try {
            addCors(ex);

            String path = ex.getRequestURI().getPath();
            String method = ex.getRequestMethod().toUpperCase();

            if (path.matches("^/artworks/?$")) {
                if ("GET".equals(method)) {
                    List<Artwork> list = service.listArtworks();
                    writeJson(ex, 200, toJson(list));
                    return;
                }

                if ("POST".equals(method)) {
                    String body = readBody(ex);

                    Integer artistId = JsonUtil.intOrNull(body, "artistId");
                    String type = JsonUtil.str(body, "type");
                    String title = JsonUtil.str(body, "title");
                    Integer year = JsonUtil.intOrNull(body, "year");
                    Double price = JsonUtil.doubleOrNull(body, "price");
                    Boolean sold = JsonUtil.boolOrNull(body, "sold");

                    String material = JsonUtil.str(body, "material");
                    String style = JsonUtil.str(body, "style");
                    String medium = JsonUtil.str(body, "medium");
                    Double weightKg = JsonUtil.doubleOrNull(body, "weightKg");

                    if (artistId == null) throw new ValidationException("artistId is required");

                    Artist artist = new Artist(artistId, "tmp", 2000, "tmp");

                    Artwork artwork = ArtworkFactory.create(
                            type,
                            null,
                            title,
                            year == null ? 0 : year,
                            price == null ? 0 : price,
                            sold != null && sold,
                            artist,
                            material,
                            style,
                            medium,
                            weightKg
                    );

                    int id = service.createArtwork(artwork);
                    writeJson(ex, 200, "{\"id\":" + id + "}");
                    return;
                }

                writeJson(ex, 405, JsonUtil.err("Use GET/POST /artworks"));
                return;
            }

            if (path.matches("^/artworks/\\d+$")) {
                int id = Integer.parseInt(path.substring("/artworks/".length()));

                if ("GET".equals(method)) {
                    Artwork a = service.getArtwork(id);
                    writeJson(ex, 200, toJson(a));
                    return;
                }

                if ("PUT".equals(method)) {
                    String body = readBody(ex);

                    Double newPrice = JsonUtil.doubleOrNull(body, "price");
                    Boolean sold = JsonUtil.boolOrNull(body, "sold");

                    if (newPrice != null) service.changePrice(id, newPrice);
                    if (sold != null) service.setSold(id, sold);

                    writeJson(ex, 200, JsonUtil.ok("updated"));
                    return;
                }

                if ("DELETE".equals(method)) {
                    service.deleteArtwork(id);
                    writeJson(ex, 200, JsonUtil.ok("deleted"));
                    return;
                }

                writeJson(ex, 405, JsonUtil.err("Use GET/PUT/DELETE /artworks/{id}"));
                return;
            }

            writeJson(ex, 404, JsonUtil.err("Not found"));

        } catch (NotFoundException e) {
            writeJson(ex, 404, JsonUtil.err(e.getMessage()));
        } catch (ValidationException e) {
            writeJson(ex, 400, JsonUtil.err(e.getMessage()));
        } catch (Exception e) {
            writeJson(ex, 500, JsonUtil.err(e.getMessage()));
        }
    }

    private static String readBody(HttpExchange ex) throws IOException {
        try (InputStream is = ex.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static void writeJson(HttpExchange ex, int status, String json) throws IOException {
        addCors(ex);
        byte[] data = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(status, data.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(data);
        }
    }

    // JSON (Artists)
    private static String toJsonArtist(Artist a) {
        return "{"
                + "\"id\":" + a.getId() + ","
                + "\"name\":\"" + JsonUtil.escape(a.getName()) + "\","
                + "\"yearOfBirth\":" + a.getYearOfBirth() + ","
                + "\"nationality\":\"" + JsonUtil.escape(a.getNationality()) + "\""
                + "}";
    }

    private static String toJsonArtists(List<Artist> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(toJsonArtist(list.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    // JSON (Artworks)
    private static String toJson(List<Artwork> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(toJson(list.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    private static String toJson(Artwork a) {
        String type = a.getClass().getSimpleName();

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\":").append(a.getId() == null ? "null" : a.getId()).append(",");
        sb.append("\"type\":\"").append(JsonUtil.escape(type)).append("\",");
        sb.append("\"title\":\"").append(JsonUtil.escape(a.getTitle())).append("\",");
        sb.append("\"year\":").append(a.getYear()).append(",");
        sb.append("\"price\":").append(a.getPrice()).append(",");
        sb.append("\"sold\":").append(a.isSold()).append(",");

        sb.append("\"artistId\":").append(a.getArtist() != null ? a.getArtist().getId() : "null").append(",");
        sb.append("\"artistName\":\"").append(JsonUtil.escape(a.getArtist() != null ? a.getArtist().getName() : "")).append("\"");

        if (a instanceof Painting p) {
            sb.append(",\"material\":\"").append(JsonUtil.escape(p.getMaterial())).append("\"");
            sb.append(",\"style\":\"").append(JsonUtil.escape(p.getStyle())).append("\"");
        } else if (a instanceof Sculpture s) {
            sb.append(",\"medium\":\"").append(JsonUtil.escape(s.getMedium())).append("\"");
            sb.append(",\"weightKg\":").append(s.getWeightKg());
        }

        sb.append("}");
        return sb.toString();
    }
}
