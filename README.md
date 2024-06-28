# Health Data Communication Library

**healthdata-coms** is a lightweight library for working with hl7 messages. Health Level Seven, abbreviated to HL7, is a
global communication standard for the transfer of clinical and administrative health data between applications.

## Table of Contents

- [Installation](#installation)
- [Usage Examples](#usage-examples)
    - [Building Hl7Message](#building-Hl7Message)
    - [Converting Hl7Message](#converting-Hl7Message)
    - [Cloning Hl7Message](#cloning-hl7message)

## Installation

To use this library in your project, add below Maven dependency to your pom.xml file. Make sure to use the
latest version available. This project is deployed to both
the [Maven Central Repository](https://central.sonatype.com/artifact/io.github.rohit-walia/hl7message) and
the[GitHub Package Registry](https://github.com/rohit-walia?tab=packages&repo_name=healthdata-coms)

```xml

<dependency>
    <groupId>io.github.rohit-walia</groupId>
    <artifactId>hl7message</artifactId>
    <version>${hl7message.version}</version>
</dependency>
```

## Usage Examples

#### Building Hl7Message

You can build Hl7Message instances from scratch using the Builder or you can build one based off an existing Builder
instance. The Hl7Message and its Segments all are Builder objects and have the `toBuilder` configuration enabled. This means
you have the ability to create a new instance of Hl7Message that starts out with all the values of another instance.

```Java
void create() {
  // create message from scratch
  Hl7Message msg1 = Hl7Message.builder()
      .msh(MSH.builder()
          .msh_3_sendingSystem("A")
          .msh_6_receivingFacility("A")
          .msh_9_messageType("A")
          .build())
      //.. etc - add more segments
      .build();

  // create message based off another message
  Hl7Message msg2 = msg1.toBuilder()
      .pid(PID.builder()
          .pid_2_patientId("123")
          .pid_3_patient_identifier_list("PAT01")
          .pid_5_patient_name(ExtendedPersonName.builder().familyName("Doe").givenName("Jane").build())
          .pid_7_patient_dob("19910619")
          .pid_8_patient_gender("M")
          .build())
      .build();
}
```

#### Converting Hl7Message

You can convert the 'Data Type' of the message via the `print()` and `fromString()` methods which all segments and the
Hl7Message object implement. The print method outputs the message as a String. The fromString method creates Hl7Message
instance from a provided String. This String-to-Object and vice-versa conversion is useful.

To convert the 'Order Type' of the message, this can be done
using [Hl7Converter](hl7message/src/main/java/org/hl7/Hl7Converter.java). It provides a convenient way to convert messages
from one type of order to another. The available order types are defined
in [MessageEvent](hl7message/src/main/java/org/hl7/common/MessageEvent.java).

```Java
void convert() {
  Hl7Message newHl7Message = Hl7Message.fromString("""
      MSH|^~\\&|||AB|LOCATION1|20240531000000||RDS^O13^RDS_O13|4154345958|P|2.5||||||ASCII|||
      PID|1|775908|08PAT||lastname^firstname^^^^||19690531000000|M|||||||||||||||||||||||||||||||
      ORC|NW||5288240975||||1^QHS&1200,1300^1^20240601111958^20240607111958^0||20240604100958|||1234567890^MedProFirstName^MedProLastName||||||||||||||||||
      RXO|Mirtazapine 7.5MG TAB|||||||||||||||||||||||||||
      RXE||69618001001^Mirtazapine 7.5MG TAB^||||TABS|^instructions||||||||58902||||||||||||F33.9^Depression^ICD10|||||||||||||||||
      TQ1|1|1^TAB|QHS|1200-1300|||20240607111958||P||Take 1 tablet my mouth every day for Depression|A||
      RXR|27^by mouth|||||
      RXD|1|69618001001^Mirtazapine 7.5MG TAB|20240607111040||||||||||||||||||||||||||||||
      """);

  // convert Hl7Message to one that is of type DISPENSE
  Hl7Message dispenseHl7Message = Hl7Converter.convert(newHl7Message, MessageEvent.ORDER_DISPENSE);

  // convert Hl7Message to String
  String dispenseHl7MessageAsStr = dispenseHl7Message.print();
}
```

#### Cloning Hl7Message

The Hl7Message and its Segments are all Jacksonized Builder Objects and therefore can easily be serialized and deserialized
via the Jackson library. Internally, this library is
leveraging [jackson-helper](https://github.com/rohit-walia/jackson-helper) to provide this feature.

Note: The [Hl7MsgUtils](hl7message/src/main/java/org/hl7/utils/Hl7MsgUtils.java) provides a convenient 'copy' method that,
as the name suggests, copies an Hl7Message object using the same approach below.

```Java
void copy() {
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
```

# Dependencies

### JUnit5

This project uses JUnit5 for testing. Tests can be found [here](hl7message/src/test/java/org/hl7).
See [here](https://junit.org/junit5/docs/current/user-guide/) for more information on JUnit5.

### Lombok

This project uses lombok to decrease boilerplate code.

### Jackson

This project is leveraging the [Jackson Helper](https://github.com/rohit-walia/jackson-helper) library for its convenient
serialization and deserialization capabilities.