package com.jpgalovic.daydream;

import com.jpgalovic.daydream.model.score.Score;

import org.junit.Test;

import static org.junit.Assert.*;

public class ScoreUnitTests {
    @Test
    public void scores_are_equal() {
        Score left = new Score("ABC", 10);
        Score right = new Score("ABC", 10);
        assertEquals(left, right);
    }

    @Test
    public void scores_are_not_equal() {
        Score left = new Score("ABC", 10);
        Score right = new Score("CDE", 10);
        assertNotEquals(left, right);
    }

    @Test
    public void scores_are_less_than() {
        Score left = new Score("ABC", 8);
        Score right = new Score("CDE", 10);
        assertTrue(left.lessThan(right));
    }

    @Test
    public void scores_are_not_less_than() {
        Score left = new Score("ABC", 10);
        Score right = new Score("CDE", 8);
        assertFalse(left.lessThan(right));
    }

    @Test
    public void get_name() {
        Score score = new Score("ABC", 10);

        assertEquals("ABC", score.getName());
    }

    @Test
    public void set_name() {
        Score score = new Score("ABC", 10);

        assertEquals("ABC", score.getName());
        assertNotEquals("DEF", score.getName());

        score.setName("DEF");

        assertNotEquals("ABC", score.getName());
        assertEquals("DEF", score.getName());
    }

    @Test
    public void get_score() {
        Score score = new Score("ABC", 10);

        assertEquals(10, score.getScore());
    }

    @Test
    public void set_score() {
        Score score = new Score("ABC", 10);

        assertEquals(10, score.getScore());
        assertNotEquals(20, score.getScore());

        score.setScore(20);

        assertNotEquals(10, score.getScore());
        assertEquals(20, score.getScore());
    }

}
