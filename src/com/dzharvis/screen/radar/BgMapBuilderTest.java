package com.dzharvis.screen.radar;

import com.dzharvis.graph.Node;
import com.dzharvis.screen.ScreenReader;
import com.dzharvis.utils.Position;
import com.dzharvis.utils.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class BgMapBuilderTest {

    @Mock
    ScreenReader reader;

    BgMapBuilder bg;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        when(reader.getNumOfPlayers()).thenReturn(1);
        when(reader.getInstanceID()).thenReturn(38);
        when(reader.isOnBG()).thenReturn(true);
    }

    @Test
    public void test() throws InterruptedException {
        bg = new BgMapBuilder(reader);
//        when(reader.getPlayerPosition(anyInt())).thenAnswer(new Answer<Position>() {
//            @Override
//            public Position answer(InvocationOnMock invocation) throws Throwable {
//                return new Position(random(), -random());
//            }
//        });
//        for(int i=0; i<5000; i++){
//            bg.readData();
//        }
        bg.save();
//
        bg = new BgMapBuilder(reader);
        bg.save();
    }

    @Test
    public void test2() throws InterruptedException {
        when(reader.getNumOfPlayers()).thenReturn(2);
        when(reader.getInstanceID()).thenReturn(388);
        when(reader.isOnBG()).thenReturn(true);
        bg = new BgMapBuilder(reader);
        when(reader.getPlayerPosition(eq(1))).thenAnswer(new Answer<Position>() {
            @Override
            public Position answer(InvocationOnMock invocation) throws Throwable {
                return new Position(1, -1);
            }
        });
        when(reader.getPlayerPosition(eq(2))).thenAnswer(new Answer<Position>() {
            int i=1;
            @Override
            public Position answer(InvocationOnMock invocation) throws Throwable {
                return new Position(1, -1*i++);
            }
        });
        when(reader.getCurrentPosition()).thenReturn(new Position(1, -1));
        for(int i=0; i<10; i++){
            bg.readData();
        }
        Node randomPlayerPos = bg.getRandomPlayerPos();
        Node currentPosition = bg.getCurrentPosition();
        List<Position> shortest = Utils.findShortest(currentPosition, randomPlayerPos);
        Assert.assertTrue(shortest.size() == 10);
    }

    private boolean verifyOrder(List<Position> shortest, Node... n) {
        for(int i=0; i<n.length; i++){
            Position p1 = shortest.get(i);
            if(!p1.equals(n[i].getPosition())) return false;
        }
        return true;
    }
}