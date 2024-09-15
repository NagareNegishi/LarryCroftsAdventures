package nz.ac.wgtn.swen225.lc.app;

import java.util.function.Function;

public class MockCamera {
    /**mock camera */
   
        private Direction direction= Direction.NONE;
        public Runnable set(Function<Direction,Direction> f){
            return ()->direction = f.apply(direction);
            }

    
}
