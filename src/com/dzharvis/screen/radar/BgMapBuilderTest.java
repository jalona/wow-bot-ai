package com.dzharvis.screen.radar;

import com.dzharvis.screen.ScreenReader;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        bg.s();
//
        bg = new BgMapBuilder(reader);
        bg.s();
    }
}