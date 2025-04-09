package events.application.port.in.request;

import com.jnulocker.events.application.port.in.request.FloorInfo;

public class FloorInfoTestDataBuilder {
    private Integer floorNumber = 1;
    private String lockerPrefix = "A";
    private Integer lockerStartNumber = 1;
    private Integer lockerEndNumber = 20;

    private FloorInfoTestDataBuilder() {}

    public static FloorInfoTestDataBuilder floorInfoBuilder() {
        return new FloorInfoTestDataBuilder();
    }

    public FloorInfoTestDataBuilder withFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
        return this;
    }

    public FloorInfoTestDataBuilder withLockerPrefix(String lockerPrefix) {
        this.lockerPrefix = lockerPrefix;
        return this;
    }

    public FloorInfoTestDataBuilder withLockerStartNumber(Integer lockerStartNumber) {
        this.lockerStartNumber = lockerStartNumber;
        return this;
    }

    public FloorInfoTestDataBuilder withLockerEndNumber(Integer lockerEndNumber) {
        this.lockerEndNumber = lockerEndNumber;
        return this;
    }

    public FloorInfo build() {
        return new FloorInfo(floorNumber, lockerPrefix, lockerStartNumber, lockerEndNumber);
    }
}
