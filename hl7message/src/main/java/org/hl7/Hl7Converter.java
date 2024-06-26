package org.hl7;

import lombok.extern.slf4j.Slf4j;
import org.hl7.common.MessageEvent;
import org.hl7.segment.RXD;
import org.hl7.segment.component.CodedElement;
import org.hl7.utils.Hl7DateUtils;
import org.hl7.utils.Hl7MsgUtils;

import java.time.LocalDateTime;

@Slf4j
public class Hl7Converter {

  /**
   * Convert hl7 message from one type to another.
   */
  public static Hl7Message convert(Hl7Message origMsg, MessageEvent convertTo) {
    switch (convertTo) {
      case ORDER_DISPENSE -> {
        return convertToDispense(origMsg);
      }
      case ORDER_DC -> {
        return convertToDiscontinue(origMsg);
      }
      case ORDER_UPDATE -> {
        return convertToUpdate(origMsg);
      }
      case ORDER_NEW, ORDER_HOLD, ORDER_IGNORE, ORDER_REFILL, ORDER_RESUME ->
          throw new RuntimeException("Can not convert message. Not yet implemented.");
      default -> throw new RuntimeException("Can not convert message. Unknown message event: " + convertTo);
    }
  }

  private static Hl7Message convertToDispense(Hl7Message origMsg) {
    // create a copy of the original hl7 message
    Hl7Message.Hl7MessageBuilder dispense = Hl7MsgUtils.copy(origMsg).toBuilder();

    // update MSH.9 - Message Type
    dispense.msh(origMsg.getMsh().toBuilder()
        .msh_9_messageType("RDS^O13^RDS_O13")
        .msh_7_messageDatetime(Hl7DateUtils.formatToHl7(LocalDateTime.now()))
        .build());

    // update ORC.1 - Order Control
    dispense.orc(origMsg.getOrc().toBuilder().orc_1_orderControl("RE").build());

    // update RXD segment
    if (origMsg.getRxd() == null) {
      log.warn("Converting message to dispense but found original message without RXD segment. "
          + "Adding RXD segment based on RXE segment.");
      if (origMsg.getRxe() == null) {
        throw new RuntimeException("Can not convert message to dispense. Missing RXE segment.");
      }
      dispense.rxd(RXD.builder()
          .rxd_1_dispenseSubIdCounter("1")
          .rxd_2_dispenseGiveCode(CodedElement.builder()
              .id(origMsg.getRxe().getRxe_2_1_drugNdc())
              .text(origMsg.getRxe().getRxe_2_2_drugName())
              .build())
          .build());
    } else {
      dispense.rxd(origMsg.getRxd().toBuilder().build());
    }

    return dispense.build();
  }

  private static Hl7Message convertToDiscontinue(Hl7Message origMsg) {
    // create a copy of the original hl7 message
    Hl7Message.Hl7MessageBuilder discontinue = Hl7MsgUtils.copy(origMsg).toBuilder();

    // update MSH.9 - Message Type
    discontinue.msh(origMsg.getMsh().toBuilder()
        .msh_9_messageType("RDE^O11^RDE_O11")
        .msh_7_messageDatetime(Hl7DateUtils.formatToHl7(LocalDateTime.now()))
        .build());

    // update ORC.1 - Order Control
    discontinue.orc(origMsg.getOrc().toBuilder().orc_1_orderControl("DC").build());

    return discontinue.build();
  }

  private static Hl7Message convertToUpdate(Hl7Message origMsg) {
    // create a copy of the original hl7 message
    Hl7Message.Hl7MessageBuilder update = Hl7MsgUtils.copy(origMsg).toBuilder();

    // update MSH.9 - Message Type
    update.msh(origMsg.getMsh().toBuilder()
        .msh_9_messageType("RDE^O11^RDE_O11")
        .msh_7_messageDatetime(Hl7DateUtils.formatToHl7(LocalDateTime.now()))
        .build());

    // update ORC.1 - Order Control
    update.orc(origMsg.getOrc().toBuilder().orc_1_orderControl("XO").build());

    return update.build();
  }
}
