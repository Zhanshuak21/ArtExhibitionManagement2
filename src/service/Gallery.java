package service;

import domain.Artwork;

import java.util.*;
import java.util.stream.Collectors;

public class Gallery<T extends Artwork> {
    private final List<T> exhibitionPieces = new ArrayList<>();

    public void addPiece(T piece) { exhibitionPieces.add(piece); }

    public List<T> listAll() { return Collections.unmodifiableList(exhibitionPieces); }

    public List<T> filterSold() {
        return exhibitionPieces.stream()
                .filter(Artwork::isSold)
                .collect(Collectors.toList());
    }

    public List<T> searchByTitle(String query) {
        String q = (query == null) ? "" : query.trim().toLowerCase();
        return exhibitionPieces.stream()
                .filter(a -> a.getTitle().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<T> sortByPriceDesc() {
        return exhibitionPieces.stream()
                .sorted(Comparator.comparingDouble(Artwork::getPrice).reversed())
                .collect(Collectors.toList());
    }

    public Optional<T> findMostExpensive() {
        return exhibitionPieces.stream()
                .max(Comparator.comparingDouble(Artwork::getPrice));
    }
}
