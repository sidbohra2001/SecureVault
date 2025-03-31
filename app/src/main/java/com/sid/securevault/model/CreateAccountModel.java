package com.sid.securevault.model;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateAccountModel implements Serializable {
    private String fullName;
    private String mobileNumber;
    private String emailId;
    private LocalDate dateOfBirth;
    private String password;
    private String confirmPassword;
}
