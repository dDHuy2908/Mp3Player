package com.huydo2908.basemodule.models;

import java.io.Serializable;

public abstract class BaseModel implements Serializable {
    public abstract boolean checkFilter(String key);
}
