package com.diploma.law.services;

import java.util.List;

public interface SearchOptionalThingsService
{

    String getDate(String text);

    String getAddress(String text);

    String getWeapon(String text);

    List<String> getNames(String text);

    List<String> getNamesForSubject(String text);
}
