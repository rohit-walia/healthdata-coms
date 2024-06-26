package org.hl7.common;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum MessageEvent {
  ORDER_NEW,
  ORDER_DC,
  ORDER_DISPENSE,
  ORDER_UPDATE,
  ORDER_IGNORE,
  ORDER_REFILL,
  ORDER_HOLD,
  ORDER_RESUME,
  @JsonEnumDefaultValue
  UNDEFINED;
}
