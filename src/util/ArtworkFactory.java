package util;

import domain.*;
import exceptions.ValidationException;

public final class ArtworkFactory {
    private ArtworkFactory() {}

    public static Artwork create(
            String type,
            Integer id,
            String title,
            int year,
            double price,
            boolean sold,
            Artist artist,
            String material,
            String style,
            String medium,
            Double weightKg
    ) {
        if (type == null) throw new ValidationException("type is required");
        String t = type.trim().toLowerCase();

        switch (t) {
            case "painting":
                return new Painting(id, title, year, price, sold, artist, material, style);
            case "sculpture":
                double w = (weightKg == null) ? 0.0 : weightKg;
                return new Sculpture(id, title, year, price, sold, artist, medium, w);
            default:
                throw new ValidationException("Unknown artwork type: " + type);
        }
    }
}
