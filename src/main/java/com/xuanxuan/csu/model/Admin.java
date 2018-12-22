package com.xuanxuan.csu.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
public class Admin {
    @Id
    @NotNull
    private String username;

    @NotNull
    private String password;
}