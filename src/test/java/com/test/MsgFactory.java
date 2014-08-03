package com.test;

import com.lmax.disruptor.EventFactory;

public class MsgFactory implements EventFactory<Msg> {

    public Msg newInstance() {
        return new Msg();
    }


}