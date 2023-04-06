package org.spsochnev.photo.service;

import java.util.Calendar;

public record PhotoDTO(String path, Calendar timestamp, long size) {}
