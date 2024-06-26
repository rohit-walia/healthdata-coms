package org.hl7;

import org.apache.commons.lang3.StringUtils;
import org.github.jacksonhelper.deserialize.DeserializeToObj;
import org.github.jacksonhelper.serialize.SerializeToStr;
import org.hl7.segment.MSH;
import org.hl7.segment.RXD;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Hl7MessageTest {

  @Test
  public void testHl7MessageBuilder_withRequiredField() {
    // pseudo message with only MSH segment w/ non-null fields
    Hl7Message msg = Hl7Message.builder()
        .msh(MSH.builder()
            .msh_3_sendingSystem("FW")
            .msh_6_receivingFacility("Facility 1")
            .msh_9_messageType("ADT^A01")
            .build())
        .build();

    // get fields that have default values when not explicitly set
    String msh7 = msg.getMsh().getMsh_7_messageDatetime();
    String msh10 = msg.getMsh().getMsh_10_messageId();

    Assertions.assertNotNull(msh7, "MSH 7 should have default value set.");
    Assertions.assertNotNull(msh10, "MSH 10 should have default value set.");
    Assertions.assertEquals("MSH|^~\\&|FW||PCC|Facility 1|" + msh7 + "||ADT^A01|"
        + msh10 + "|P|2.5||||||ASCII|||", msg.printMessage());
  }

  @Test
  public void testHl7MessageBuilder_withoutRequiredField() {
    Assertions.assertThrows(NullPointerException.class, () -> Hl7Message.builder().rxd(RXD.builder().build()).build());
  }

  @Test
  public void testHl7Message_IsMutable() {
    Hl7Message msg = Hl7Message.builder()
        .msh(MSH.builder()
            .msh_3_sendingSystem("FW")
            .msh_6_receivingFacility("Facility 1")
            .msh_9_messageType("ADT^A01")
            .build())
        .build();

    msg.setMsh(MSH.builder()
        .msh_3_sendingSystem("FW")
        .msh_6_receivingFacility("Facility 2")
        .msh_9_messageType("RDS^013")
        .build());

    String msh7 = msg.getMsh().getMsh_7_messageDatetime();
    String msh10 = msg.getMsh().getMsh_10_messageId();

    Assertions.assertEquals("MSH|^~\\&|FW||PCC|Facility 2|" + msh7 + "||RDS^013|" + msh10
        + "|P|2.5||||||ASCII|||", msg.printMessage());
  }

  @Test
  public void testHl7Message_CanDeepCopy_WithJackson() {
    Hl7Message msg1 = Hl7Message.builder()
        .msh(MSH.builder()
            .msh_3_sendingSystem("FW")
            .msh_6_receivingFacility("Facility 1")
            .msh_9_messageType("ADT^A01")
            .build())
        .build();

    Hl7Message msg2 = DeserializeToObj.fromString(SerializeToStr.toString(msg1), Hl7Message.class);

    // both message instances should have identical data but are not the same object
    Assertions.assertEquals(msg1.printMessage(), msg2.printMessage());
    Assertions.assertNotEquals(msg1.hashCode(), msg2.hashCode());
  }

  @Test
  public void testHl7Message_CanDeepCopy_WithLombokToBuilder() {
    Hl7Message msg1 = Hl7Message.builder()
        .msh(MSH.builder()
            .msh_3_sendingSystem("FW")
            .msh_6_receivingFacility("Facility 1")
            .msh_9_messageType("ADT^A01")
            .build())
        .build();

    Hl7Message msg2 = msg1.toBuilder().build();

    msg2.setMsh(MSH.builder()
        .msh_3_sendingSystem("FW")
        .msh_6_receivingFacility("Facility 2")
        .msh_9_messageType("ADT^A02")
        .build());

    // both message instances should have identical data but are not the same object
    Assertions.assertNotEquals(msg1.printMessage(), msg2.printMessage());
    Assertions.assertNotEquals(msg1.hashCode(), msg2.hashCode());
  }

  @SuppressWarnings("checkstyle:lineLength")
  @Test
  public void testHl7Message_CanConvertFromString() {
    String originalHl7 = """
        MSH|^~\\&|PC||PCC|QTFCMORMEFAC25|20240607111040||RDS^O13^RDS_O13|4154958|P|2.5||||||ASCII|||
        PID|1|775908|02RES||Cooper_dc0f^QTF_Bradley_2692^^^^||19360531000000|M|||||||||||||||||||||||||||||||
        ORC|RE||5288240975||||1^QHS&1200,1300^1^20240601111958^20240607111958^0||20240604100958|||1234567890^QTF_MedProFirstName^QTF_MedProLastName||||||||||||||||||
        RXO|Mirtazapine 7.5MG TAB|||||||||||||||||||||||||||
        RXE||69618001001^Mirtazapine 7.5MG TAB^||||TABS|^Default RXE.7 instructions. Msg fails without this.||||||||58902||||||||||||F33.9^Depression^ICD10|||||||||||||||||
        TQ1|1|1^TAB|QHS|1200-1300|||20240607111958||P||Take 1 tablet my mouth every day for Depression|A||
        RXR|27^by mouth|||||
        RXD|1|69618001001^Mirtazapine 7.5MG TAB|20240607111040||||||||||||||||||||||||||||||
        """;

    String convertedHl7 = Hl7Message.fromString(originalHl7)
        .printMessage()
        .replace("\r\n", "\n")
        .replace('\r', '\n');

    String diff = StringUtils.difference(originalHl7, convertedHl7);
    Assertions.assertEquals("", diff);
  }
}