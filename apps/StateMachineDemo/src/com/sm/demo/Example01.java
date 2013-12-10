package com.sm.demo;

import com.sm.demo.turing.StateFactory;
import com.sm.demo.turing.StateMachine;

public class Example01 {

    /**
     * <code>
     * state machine sm = new state machine()<br/>
     * State sitdownState = stateFactory.create(sitdown);<br/>
     * sitdownState.setBeginAction(undressAction);<br/>
     * sitDownState.setEndAction(CallWaiterAction);<br/>
     * sitDownState.printInfo();<br/>
     * --------------------<br/>
     * state name: site down<br/>
     * class name: SitDownState<br/>
     * ID        : 1<br/>
     * BeginAction: undress action<br/>
     * End Action : call waiter action.<br/>
     * Event count: 3<br/>
     * Event detail:<br/>
     *     index    input    changeto<br/>
     *     1        waiterComingAction    chooseFoodState<br/>
     *     
     * --------------------<br/>
     * sm.initState(state sitdownState);<br/>
     * State sitdownState = sm.getState(init);<br/>
     * chooseFoodState= stateFactory.create(chooseFoodState);<br/>
     * sitdownState.when(waiterComingAction).changeTo(chooseFoodState).commit();<br/>
     * <br/>
     * waitFoodState = stateFactory.create(waitFood);<br/>
     * chooseFoodState.when(submitChoosedFoodAction).changeTo(waitFoodState).commit();<br/>
     * waitFoodState.<br/>
     * 
     * 
     * State setDownState = factory.create("坐下");
        setDownState.setBeginAction(new Action("脱去外套"));
        setDownState.setEndAction(new Action("叫服务员来点菜"));
        State chooseFoodState = factory.create("开始点菜");
        setDownState.when(new Event("服务员来了")).changeTo(chooseFoodState).commit();

        State waitFoodState = factory.create("等菜");
        chooseFoodState.when(new Event("点完菜了")).changeTo(waitFoodState).commit();

        State eatFoodState = factory.create("吃饭");
        waitFoodState.when(new Event("菜来了")).changeTo(eatFoodState).commit();
     * </code>
     */
    public static void main(String[] args) {

        StateMachine machine = new StateMachine();
        StateFactory factory = StateFactory.getInstance();
        // from set to eat food state.

    }
}
