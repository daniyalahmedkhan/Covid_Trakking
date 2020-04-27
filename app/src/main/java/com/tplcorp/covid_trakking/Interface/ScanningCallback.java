package com.tplcorp.covid_trakking.Interface;

import com.tplcorp.covid_trakking.Model.Connections;

import java.util.List;

public interface ScanningCallback {

   void updateScanningData(List<Connections> connectionsList);

}
