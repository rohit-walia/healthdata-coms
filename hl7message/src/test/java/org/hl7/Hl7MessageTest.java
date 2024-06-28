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
    var sendingSystem = "AB";
    var receivingFacility = "Location 1";
    var messageType = "ADT^A01";

    // pseudo message with only MSH segment w/ non-null fields
    Hl7Message msg = Hl7Message.builder()
        .msh(MSH.builder()
            .msh_3_sendingSystem(sendingSystem)
            .msh_6_receivingFacility(receivingFacility)
            .msh_9_messageType(messageType)
            .build())
        .build();

    // get fields that have default values when not explicitly set
    String msh7 = msg.getMsh().getMsh_7_messageDatetime();
    String msh10 = msg.getMsh().getMsh_10_messageId();
    String msh12 = msg.getMsh().getMsh_12_versionId();

    Assertions.assertNotNull(msh7, "MSH 7 should have default value set.");
    Assertions.assertNotNull(msh10, "MSH 10 should have default value set.");
    Assertions.assertNotNull(msh12, "MSH 12 should have default value set.");
    Assertions.assertEquals("MSH|^~\\&|" + sendingSystem + "|||" + receivingFacility + "|" + msh7 + "||"
        + messageType + "|" + msh10 + "|P|" + msh12 + "||||||ASCII|||", msg.printMessage());
  }

  @Test
  public void testHl7MessageBuilder_withoutRequiredField() {
    Assertions.assertThrows(NullPointerException.class, () -> Hl7Message.builder().rxd(RXD.builder().build()).build());
  }

  @Test
  public void testHl7Message_IsMutable() {
    var sendingSystem = "AB";
    var receivingFacility = "Location 2";
    var messageType = "RDS^013";

    Hl7Message msg = Hl7Message.builder()
        .msh(MSH.builder()
            .msh_3_sendingSystem(sendingSystem)
            .msh_6_receivingFacility("Facility 1")
            .msh_9_messageType("ADT^A01")
            .build())
        .build();

    msg.printMessage();

    msg.setMsh(MSH.builder()
        .msh_3_sendingSystem(sendingSystem)
        .msh_6_receivingFacility(receivingFacility)
        .msh_9_messageType(messageType)
        .build());

    String msh7 = msg.getMsh().getMsh_7_messageDatetime();
    String msh10 = msg.getMsh().getMsh_10_messageId();
    String msh12 = msg.getMsh().getMsh_12_versionId();

    Assertions.assertEquals("MSH|^~\\&|" + sendingSystem + "|||" + receivingFacility + "|" + msh7 + "||"
        + messageType + "|" + msh10 + "|P|" + msh12 + "||||||ASCII|||", msg.printMessage());
  }

  @Test
  public void testHl7Message_CanDeepCopy_WithJackson() {
    Hl7Message msg1 = Hl7Message.builder()
        .msh(MSH.builder()
            .msh_3_sendingSystem("AB")
            .msh_6_receivingFacility("Location 3")
            .msh_9_messageType("ADT^A01")
            .build())
        .build();

    String serializedMessage = SerializeToStr.toString(msg1);
    
    Hl7Message msg2 = DeserializeToObj.fromString(serializedMessage, Hl7Message.class);

    // both message instances should have identical data but are not the same object
    Assertions.assertEquals(msg1.printMessage(), msg2.printMessage());
    Assertions.assertNotEquals(msg1.hashCode(), msg2.hashCode());
  }

  @Test
  public void testHl7Message_CanDeepCopy_WithLombokToBuilder() {
    Hl7Message msg1 = Hl7Message.builder()
        .msh(MSH.builder()
            .msh_3_sendingSystem("A")
            .msh_6_receivingFacility("A")
            .msh_9_messageType("A")
            .build())
        .build();

    Hl7Message msg2 = msg1.toBuilder().msh(MSH.builder()
        .msh_3_sendingSystem("B")
        .msh_6_receivingFacility("B")
        .msh_9_messageType("B")
        .build()).build();

    // both message instances should have identical data but are not the same object
    Assertions.assertNotEquals(msg1.printMessage(), msg2.printMessage());
    Assertions.assertNotEquals(msg1.hashCode(), msg2.hashCode());
  }

  @SuppressWarnings("checkstyle:lineLength")
  @Test
  public void testHl7Message_CanConvertFromString() {
    String originalHl7 = """
        MSH|^~\\&|||AB|LOCATION1|20240607111040||RDS^O13^RDS_O13|4154345958|P|2.5||||||ASCII|||
        PID|1|775908|08PAT||lastname^firstname^^^^||19690531000000|M|||||||||||||||||||||||||||||||
        ORC|RE||5288240975||||1^QHS&1200,1300^1^20240601111958^20240607111958^0||20240604100958|||1234567890^MedProFirstName^MedProLastName||||||||||||||||||
        RXO|Mirtazapine 7.5MG TAB|||||||||||||||||||||||||||
        RXE||69618001001^Mirtazapine 7.5MG TAB^||||TABS|^instructions||||||||58902||||||||||||F33.9^Depression^ICD10|||||||||||||||||
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