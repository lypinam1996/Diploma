package com.diploma.law.services;

import com.diploma.law.dto.MainDto;

import java.util.ArrayList;

public interface ParseService
{
    MainDto parseText(String text);
}
