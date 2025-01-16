package com.GestionDossiers.demande_autorisation.deserialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.Coordinate;

import java.io.IOException;

public class MultiPolygonSerializer extends JsonSerializer<MultiPolygon> {

    @Override
    public void serialize(MultiPolygon value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // Commencer à écrire l'objet JSON
        gen.writeStartObject();

        // Serialisation des coordonnées du MultiPolygon
        gen.writeFieldName("coordinates");
        gen.writeStartArray();

        // Parcourir chaque Polygone dans le MultiPolygon
        for (int i = 0; i < value.getNumGeometries(); i++) {
            Polygon polygon = (Polygon) value.getGeometryN(i);
            gen.writeStartArray();  // Démarrer un tableau pour chaque Polygone

            // Parcourir les coordonnées du Polygone
            for (Coordinate coordinate : polygon.getCoordinates()) {
                gen.writeStartArray();
                gen.writeNumber(coordinate.x); // Longitude
                gen.writeNumber(coordinate.y); // Latitude
                gen.writeEndArray();
            }

            gen.writeEndArray(); // Fin du tableau du Polygone
        }

        gen.writeEndArray(); // Fin du tableau des Polygones

        gen.writeEndObject(); // Fin de l'objet JSON
    }
}