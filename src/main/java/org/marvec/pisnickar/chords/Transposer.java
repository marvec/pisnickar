package org.marvec.pisnickar.chords;

import java.util.StringTokenizer;

/**
 *
 * @author marvec
 */
public class Transposer {

    private static String[] notes = new String[]{"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "Hb", "H"};

    public static String normalizeChord(String chord) {
        if (chord.startsWith("B")) { // B je Hes
            chord.replaceFirst("B", "Hb");
        } else if (chord.startsWith("b")) {
            chord.replaceFirst("b", "hb");
        }

        if (chord.startsWith("is", 1)) {
            chord = chord.substring(0, 1) + "#" + chord.substring(3);
        } else if (chord.startsWith("es", 1) || (chord.startsWith("s", 1) && !chord.startsWith("sus", 1))) {
            chord = chord.substring(0, 1) + "b" + chord.substring(3);
        }

        if (chord.substring(0, 1).matches("[cdefgah]")) { // jazzova notace moll
            if (chord.length() > 1 && (chord.substring(1, 2).equals("b") || chord.substring(1, 2).equals("#"))) {
                chord = chord.substring(0, 1).toUpperCase() + chord.substring(1, 2) + "mi" +
                        (chord.length() > 2 ? chord.substring(2) : "");
            } else {
                chord = chord.substring(0, 1).toUpperCase() + "mi" + (chord.length() > 1 ? chord.substring(1) : "");
            }
        }

        if (chord.startsWith("Db")) {
            chord.replaceFirst("Db", "C#");
        } else if (chord.startsWith("Eb")) {
            chord.replaceFirst("Eb", "D#");
        } else if (chord.startsWith("Gb")) {
            chord.replaceFirst("Gb", "F#");
        } else if (chord.startsWith("Ab")) {
            chord.replaceFirst("Ab", "G#");
        } else if (chord.startsWith("Fb")) {
            chord.replaceFirst("Fb", "E");
        } else if (chord.startsWith("Cb")) {
            chord.replaceFirst("Cb", "H");
        } else if (chord.startsWith("E#")) {
            chord.replaceFirst("E#", "F");
        } else if (chord.startsWith("A#")) {
            chord.replaceFirst("A#", "Hb");
        } else if (chord.startsWith("H#")) {
            chord.replaceFirst("H#", "C");
        }

        return chord;
    }

    public static String getNormalizedChordRoot(String chord) {
        if (chord.startsWith("#", 1) || chord.startsWith("b", 1)) {
            return chord.substring(0, 2);
        } else {
            return chord.substring(0, 1);
        }
    }

    private static int normalizeNoteIndex(int note) {
        while (note >= notes.length) {
            note -= notes.length;
        }
        while (note < 0) {
            note += notes.length;
        }
        return note;
    }

    private static String transposeRoot(String root, int count) {
        int note = 0;
        for (int i = 0; i < notes.length; i++) {
            if (notes[i].equals(root)) {
                note = i;
                break;
            }
        }

        note += count;
        note = normalizeNoteIndex(note);

        return notes[note];
    }

    public static String transposeChord(String chord, int count) {
        count = normalizeNoteIndex(count);
        String normChord = normalizeChord(chord);
        String root = getNormalizedChordRoot(normChord);
        String transpRoot = transposeRoot(root, count);

        return normChord.replaceFirst(root, transpRoot);
    }

    public static String transpose(String text, int count) {
        StringTokenizer st = new StringTokenizer(text, "{}", true);
        StringBuilder out = new StringBuilder();
        boolean insideChord = false;

        while (st.hasMoreTokens()) {
            String token = st.nextToken();

            if ("{}".contains(token)) {
                insideChord = token.equals("{");
                out.append(token);
            } else if (insideChord) {
                out.append(transposeChord(token, count));
            } else {
                out.append(token);
            }
        }

        return out.toString();
    }
}
