package com.sm.demo.turing;

import java.util.ArrayList;

/**
 * <code>
 *   现实与理论之前存在一个临界，这个临界就是计算机的虚拟世界，可以理解为，现实与数学的临界是方法抽象。所以难点在于方法抽象，对图灵机的合理实现和正确使用是抽象的关键。
 * </code>
 * 
 * @author wangyue.wy
 * @since 2013-11-29
 */
public class StateMachine extends Element {

    private final ArrayList<State> states = new ArrayList<State>();

    public void addState(State state) {
        currentState = state;
        states.add(state);
    }

    public State currentState = null;

}