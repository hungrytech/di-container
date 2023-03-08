package org.hungrytech;

import beans.ApplicationContext;
import core.VentiApplication;

public class HungryTechApplication {


    public static void main(String[] args) {
        ApplicationContext applicationContext = VentiApplication.run(HungryTechApplication.class);
    }
}
