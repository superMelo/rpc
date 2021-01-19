package com.qyf.rpc.web.provider.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoUser implements Serializable{

	private static final long serialVersionUID = 1L;

	private String id;

    private String name;

    private String address;

}

