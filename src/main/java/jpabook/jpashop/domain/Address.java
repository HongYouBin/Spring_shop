package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address(){} //기본 생성자 필요

    public Address(String city, String street, String zipCode){
        this.city= city;
        this.street = street;
        this.zipcode = zipCode;
    }
}
