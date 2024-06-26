package org.hl7;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hl7.segment.MSH;
import org.hl7.segment.TQ1;
import org.hl7.utils.Hl7DateUtils;
import org.hl7.utils.Hl7MsgUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class Hl7MsgUtilsTest {

  @Test
  void testCopy_ReturnsDeepCopy() {
    Hl7Message msg1 = Hl7Message.builder()
        .msh(MSH.builder()
            .msh_3_sendingSystem("FW")
            .msh_6_receivingFacility("Facility 1")
            .msh_9_messageType("ADT^A01")
            .build())
        .build();

    Hl7Message msg2 = Hl7MsgUtils.copy(msg1);

    // both message instances should have identical data but are not the same object
    Assertions.assertEquals(msg1.printMessage(), msg2.printMessage());
    Assertions.assertNotEquals(msg1.hashCode(), msg2.hashCode());
  }

  @Test
  void testUpdateMessageStartAndEndDate() {
    Hl7Message msg1 = Hl7Message.builder()
        .msh(MSH.builder()
            .msh_3_sendingSystem("FW")
            .msh_6_receivingFacility("Facility 1")
            .msh_9_messageType("ADT^A01")
            .build())
        .tq1(TQ1.builder()
            .tq1_1_setId("1")
            .tq1_11_admin_instructions("Admin Instructions")
            .build()
            .toList())
        .build();

    LocalDateTime startDate = LocalDateTime.now();
    LocalDateTime endDate = startDate.plusDays(1);
    Hl7MsgUtils.updateMessageStartAndEndDate(msg1, startDate, endDate);

    Assertions.assertEquals(msg1.getTq1().get(0).getTq1_7_startDateTime(), Hl7DateUtils.formatToHl7(startDate));
    Assertions.assertEquals(msg1.getTq1().get(0).getTq1_8_endDateTime(), Hl7DateUtils.formatToHl7(endDate));
  }

  @Test
  void getCompositeFieldString() {
    assertEquals("", Hl7MsgUtils.getCompositeFieldString());
    assertEquals("", Hl7MsgUtils.getCompositeFieldString(""));
    assertEquals("", Hl7MsgUtils.getCompositeFieldString(null, null, null));
    assertEquals("a", Hl7MsgUtils.getCompositeFieldString("a"));
    assertEquals("", Hl7MsgUtils.getCompositeFieldString("", "", ""));
    assertEquals("a^b", Hl7MsgUtils.getCompositeFieldString("a", "b", "", ""));
    assertEquals("a^b^^c", Hl7MsgUtils.getCompositeFieldString("a", "b", "", "c"));
    assertEquals("^a^b", Hl7MsgUtils.getCompositeFieldString("", "a", "b"));
    assertEquals("^a^b", Hl7MsgUtils.getCompositeFieldString(null, "a", "b"));
    assertEquals("a^^b", Hl7MsgUtils.getCompositeFieldString("a", null, "b"));
  }
}