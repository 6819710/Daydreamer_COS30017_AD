package com.jpgalovic.daydreamer.model.game.text;

import android.content.Context;

import com.jpgalovic.daydreamer.model.TexturedMeshObject;

import java.util.ArrayList;

/**
 * Object to select a letter.
 */
public class Letter {
    private static final String TAG = "Letter";
    private static final String[] letterString = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
    };

    private ArrayList<TexturedMeshObject> letterObjects;
    private int letter;

    public Letter(Context context, int objectPositionParam, int objectUvParam, float x, float y, float z, String letter) {
        letterObjects = new ArrayList<>();

        String[] letterObj = {
                "obj/char/a.obj", "obj/char/b.obj", "obj/char/c.obj", "obj/char/d.obj",
                "obj/char/e.obj", "obj/char/f.obj", "obj/char/g.obj", "obj/char/h.obj",
                "obj/char/i.obj", "obj/char/j.obj", "obj/char/k.obj", "obj/char/l.obj",
                "obj/char/m.obj", "obj/char/n.obj", "obj/char/o.obj", "obj/char/p.obj",
                "obj/char/q.obj", "obj/char/r.obj", "obj/char/s.obj", "obj/char/t.obj",
                "obj/char/u.obj", "obj/char/v.obj", "obj/char/w.obj", "obj/char/x.obj",
                "obj/char/y.obj", "obj/char/z.obj",
        };

        String[] letterTex = {
                "obj/char/a.png", "obj/char/b.png", "obj/char/c.png", "obj/char/d.png",
                "obj/char/e.png", "obj/char/f.png", "obj/char/g.png", "obj/char/h.png",
                "obj/char/i.png", "obj/char/j.png", "obj/char/k.png", "obj/char/l.png",
                "obj/char/m.png", "obj/char/n.png", "obj/char/o.png", "obj/char/p.png",
                "obj/char/q.png", "obj/char/r.png", "obj/char/s.png", "obj/char/t.png",
                "obj/char/u.png", "obj/char/v.png", "obj/char/w.png", "obj/char/x.png",
                "obj/char/y.png", "obj/char/z.png",
        };

        for(int i = 0; i < 26; i++) {
            letterObjects.add(new TexturedMeshObject(context, letterString[i], letterObj[i], letterTex[i], objectPositionParam, objectUvParam, x, y, z));
        }

        setLetter(letter);
    }

    public String getLetter() {
        return letterString[letter];
    }

    public void setLetter(int letter) {
        this.letter = letter;
    }

    /**
     * Increments to the next letter.
     * Letter rolls over from Z -> A.
     */
    public void next() {
        ++letter;
        if(letter >= 26) {
            letter = 0;
        }
    }

    /**
     * Decrements to the previous letter.
     * Letter rolls over from A -> Z.
     */
    public void prev() {
        --letter;
        if(letter < 0) {
            letter = 25;
        }
    }

    public void setLetter(String letter) {
        this.letter = 0;
        for(int i = 0; i < 26; i++) {
            if(letterString[i] == letter) {
                this.letter = i;
                return;
            }
        }
    }

    public void draw(float[] perspective, float[] view, int objectProgram, int objectModelViewProjectionParam) {
        letterObjects.get(letter).draw(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
    }
}
