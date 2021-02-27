package com.doozycod.roadsidegenius.Utils;

import com.doozycod.roadsidegenius.Model.Cars.CarModel;
import com.doozycod.roadsidegenius.Model.Cars.Sheet1;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cars {
    String Type;

    public Cars() {
    }

    public Cars(String type) {
        Type = type;
    }

    public final List<String> GMC_LIST = new ArrayList<>();
    public final List<String> AUDI_LIST = new ArrayList<>();
    public final List<String> HYUNDAI_LIST = new ArrayList<>();
    public final List<String> HONDA_LIST = new ArrayList<>();
    public final List<String> MERCEDES_LIST = new ArrayList<>();
    public final List<String> NISSAN_LIST = new ArrayList<>();
    public final List<String> VOLKSWAGEN_LIST = new ArrayList<>();
    public final List<String> SUBARU_LIST = new ArrayList<>();
    public final List<String> CADILLAC_LIST = new ArrayList<>();
    public final List<String> TOYOTA_LIST = new ArrayList<>();
    public final List<String> LAMBORGHINI_LIST = new ArrayList<>();
    public final List<String> LINCOLN_LIST = new ArrayList<>();
    public final List<String> BENTLEY_LIST = new ArrayList<>();
    public final List<String> CHEVROLET_LIST = new ArrayList<>();
    public final List<String> FORD_LIST = new ArrayList<>();
    public final List<String> KIA_LIST = new ArrayList<>();
    public final List<String> BUICK_LIST = new ArrayList<>();
    public final List<String> PORSCHE_LIST = new ArrayList<>();
    public final List<String> DODGE_LIST = new ArrayList<>();
    public final List<String> JEEP_LIST = new ArrayList<>();
    public final List<String> MINI_LIST = new ArrayList<>();
    public final List<String> ROLLS_ROYCE_LIST = new ArrayList<>();
    public final List<String> MAZDA_LIST = new ArrayList<>();
    public final List<String> ASTON_MARTIN_LIST = new ArrayList<>();
    public final List<String> LAND_ROVER_LIST = new ArrayList<>();
    public final List<String> MITSUBISHI_LIST = new ArrayList<>();
    public final List<String> JAGUAR_LIST = new ArrayList<>();
    public final List<String> LEXUS_LIST = new ArrayList<>();
    public final List<String> SMART_LIST = new ArrayList<>();
    public final List<String> GENESIS_LIST = new ArrayList<>();
    public final List<String> MASERATI_LIST = new ArrayList<>();
    public final List<String> ALFA_ROMEO_LIST = new ArrayList<>();
    public final List<String> FERRARI_LIST = new ArrayList<>();
    public final List<String> BMW_LIST = new ArrayList<>();
    public final List<String> ACURA_LIST = new ArrayList<>();
    public final List<String> TESLA_LIST = new ArrayList<>();
    public final List<String> CHRYSLER_LIST = new ArrayList<>();
    public final List<String> RAM_LIST = new ArrayList<>();
    public final List<String> INFINITI_LIST = new ArrayList<>();
    public final List<String> RIVIAN_LIST = new ArrayList<>();
    public final List<String> VOLVO_LIST = new ArrayList<>();
    public final List<String> FIAT_LIST = new ArrayList<>();
    public final List<String> MCLAREN_LIST = new ArrayList<>();

    public final List<String> CAR_BRANDS = new ArrayList<>();
    public final List<String> CAR_YEAR_LIST = new ArrayList<>();
    public final List<String> CAR_COLORS_LIST = new ArrayList<>();

    public List<String> carColorsList() {
        CAR_COLORS_LIST.add("Select color");
        CAR_COLORS_LIST.add("White");
        CAR_COLORS_LIST.add("Blue");
        CAR_COLORS_LIST.add("Red");
        CAR_COLORS_LIST.add("Orange");
        CAR_COLORS_LIST.add("Yellow");
        CAR_COLORS_LIST.add("Black");
        CAR_COLORS_LIST.add("Purple");
        CAR_COLORS_LIST.add("Brown");
        CAR_COLORS_LIST.add("Green");
        CAR_COLORS_LIST.add("Grey");
        CAR_COLORS_LIST.add("Silver");

        return CAR_COLORS_LIST;
    }


    public List<String> getCarModelYearList() {
        CAR_YEAR_LIST.add("Select Year");
        for (int i = 2030; i >= 1960; i--) {
            CAR_YEAR_LIST.add(i + "");
        }
        return CAR_YEAR_LIST;
    }

    public List<String> getCarBrands() {
        String json = "{\n" +
                "    \"Sheet1\": [\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Audi\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"GMC\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Hyundai\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Honda\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Mercedes-Benz\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Nissan\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Volkswagen\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Subaru\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Cadillac\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Toyota\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Lamborghini\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Lincoln\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Bentley\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Chevrolet\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Ford\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Kia\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Buick\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Porsche\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Dodge\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Jeep\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"MINI\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Rolls-Royce\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"MAZDA\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Aston Martin\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Land Rover\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Mitsubishi\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Jaguar\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Lexus\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"smart\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Genesis\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Maserati\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Alfa Romeo\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Ferrari\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"BMW\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Acura\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Tesla\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Chrysler\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Ram\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"INFINITI\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Rivian\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"Volvo\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"FIAT\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Vehicle_Make\": \"McLaren\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        Gson gson = new Gson();
        CarModel carModel = gson.fromJson(json, CarModel.class);
        CAR_BRANDS.add("Select Car Brand");
        for (Sheet1 name : carModel.getSheet1()) {
            CAR_BRANDS.add(name.getVehicleMake());
        }

        return CAR_BRANDS;
    }


    //    MACLAREN CAR MODELS
    public List<String> macLarenModels() {
        MCLAREN_LIST.add("570GT");
        MCLAREN_LIST.add("570S");
        MCLAREN_LIST.add("600LT");
        MCLAREN_LIST.add("720S");

        return MCLAREN_LIST;
    }

    //    FIAT_LIST CAR MODELS
    public List<String> fiatModels() {
        FIAT_LIST.add("124 Spider");
        FIAT_LIST.add("500");
        FIAT_LIST.add("500 Abarth");
        FIAT_LIST.add("500c");
        FIAT_LIST.add("500c Abarth");
        FIAT_LIST.add("500e");
        FIAT_LIST.add("500L");
        FIAT_LIST.add("500X");


        return FIAT_LIST;
    }

    //    VOLVO_LIST CAR MODELS
    public List<String> volvoModels() {
        VOLVO_LIST.add("S60");
        VOLVO_LIST.add("S90");
        VOLVO_LIST.add("V60");
        VOLVO_LIST.add("V90");
        VOLVO_LIST.add("XC40");
        VOLVO_LIST.add("XC60");
        VOLVO_LIST.add("XC90");


        return VOLVO_LIST;
    }

    //    INFINITI_LIST CAR MODELS
    public List<String> rivianModels() {
        RIVIAN_LIST.add("R1S");
        RIVIAN_LIST.add("R1T");
        RIVIAN_LIST.add("QX60");
        RIVIAN_LIST.add("QX80");


        return RIVIAN_LIST;
    }


    //    INFINITI_LIST CAR MODELS
    public List<String> infinitiModels() {
        INFINITI_LIST.add("QX30");
        INFINITI_LIST.add("QX50");
        INFINITI_LIST.add("QX60");
        INFINITI_LIST.add("QX80");


        return INFINITI_LIST;
    }

    //    RAM CAR MODELS
    public List<String> ramModels() {
        RAM_LIST.add("ProMaster Cargo Van");
        RAM_LIST.add("ProMaster City");
        RAM_LIST.add("ProMaster Window Van");
        RAM_LIST.add("1500 Classic Crew Cab");
        RAM_LIST.add("1500 Classic Quad Cab");
        RAM_LIST.add("1500 Classic Regular Cab");
        RAM_LIST.add("1500 Crew Cab");
        RAM_LIST.add("1500 Quad Cab");
        RAM_LIST.add("2500 Crew Cab");
        RAM_LIST.add("2500 Mega Cab");
        RAM_LIST.add("2500 Regular Cab");
        RAM_LIST.add("3500 Crew Cab");
        RAM_LIST.add("3500 Mega Cab");
        RAM_LIST.add("3500 Regular Cab");


        return RAM_LIST;
    }

    //    CHRYSLER_LIST CAR MODELS
    public List<String> chrylerModels() {
        CHRYSLER_LIST.add("Pacifica");
        CHRYSLER_LIST.add("Pacifica Hybrid");
        CHRYSLER_LIST.add("Voyager");
        CHRYSLER_LIST.add("300");

        return CHRYSLER_LIST;
    }

    //      TESLA CAR MODELS
    public List<String> teslaModels() {
        TESLA_LIST.add("Model 3");
        TESLA_LIST.add("Model S");
        TESLA_LIST.add("Model X");
        TESLA_LIST.add("Model Y");


        return TESLA_LIST;
    }

    //    ACURA CAR MODELS
    public List<String> acuraModels() {
        ACURA_LIST.add("ILX");
        ACURA_LIST.add("MDX");
        ACURA_LIST.add("MDX Sport Hybrid");
        ACURA_LIST.add("RDX");
        ACURA_LIST.add("RLX");
        ACURA_LIST.add("RLX Sport Hybrid");
        ACURA_LIST.add("TLX");
        return ACURA_LIST;
    }

    //    BMW CAR MODELS
    public List<String> bmwModels() {
        BMW_LIST.add("i3");
        BMW_LIST.add("i8");
        BMW_LIST.add("M2");
        BMW_LIST.add("M4");
        BMW_LIST.add("M5");
        BMW_LIST.add("M6");
        BMW_LIST.add("X1");
        BMW_LIST.add("X2");
        BMW_LIST.add("X3");
        BMW_LIST.add("X3 M");
        BMW_LIST.add("X4");
        BMW_LIST.add("X5");
        BMW_LIST.add("X6");
        BMW_LIST.add("X6 M");
        BMW_LIST.add("X7");
        BMW_LIST.add("Z4");
        BMW_LIST.add("2 Series");
        BMW_LIST.add("3 Series");
        BMW_LIST.add("4 Series");
        BMW_LIST.add("5 Series");
        BMW_LIST.add("6 Series");
        BMW_LIST.add("7 Series");
        BMW_LIST.add("8 Series");


        return BMW_LIST;

    }

    //    FERRARI CAR MODELS
    public List<String> ferarriModels() {
        FERRARI_LIST.add("GTC4Lusso");
        FERRARI_LIST.add("488 GTB");
        FERRARI_LIST.add("488 Spider");
        FERRARI_LIST.add("812 Superfast");

        return FERRARI_LIST;

    }

    //   ALFA_ROMEO_LIST CAR MODELS
    public List<String> alfaRomeoModels() {
        ALFA_ROMEO_LIST.add("Giulia");
        ALFA_ROMEO_LIST.add("Stelvio");
        ALFA_ROMEO_LIST.add("4C Spider");


        return ALFA_ROMEO_LIST;

    }

    //    MASERATI CAR MODELS
    public List<String> maseratiModels() {
        MASERATI_LIST.add("Ghibli");
        MASERATI_LIST.add("GranTurismo");
        MASERATI_LIST.add("Levante");
        MASERATI_LIST.add("Quattroporte");


        return MASERATI_LIST;

    }

    //    GENESIS CAR MODELS
    public List<String> genesisModels() {
        GENESIS_LIST.add("G70");
        GENESIS_LIST.add("G80");
        GENESIS_LIST.add("G90");
        GENESIS_LIST.add("GV80");

        return GENESIS_LIST;
    }

    //    SMART CAR MODELS
    public List<String> smartModels() {
        SMART_LIST.add("fortwo EQ cabrio");
        SMART_LIST.add("fortwo EQ coupe");


        return SMART_LIST;
    }

    //    LEXUS CAR MODELS
    public List<String> lexusModels() {
        LEXUS_LIST.add("ES");
        LEXUS_LIST.add("GS");
        LEXUS_LIST.add("GX");
        LEXUS_LIST.add("IS");
        LEXUS_LIST.add("LC");
        LEXUS_LIST.add("LS");
        LEXUS_LIST.add("LX");
        LEXUS_LIST.add("NX");
        LEXUS_LIST.add("RC");
        LEXUS_LIST.add("RX");
        LEXUS_LIST.add("UX");


        return LEXUS_LIST;
    }

    //    JAGUAR_LIST CAR MODELS
    public List<String> jaguarModels() {
        JAGUAR_LIST.add("E-PACE");
        JAGUAR_LIST.add("F-PACE");
        JAGUAR_LIST.add("F-TYPE");
        JAGUAR_LIST.add("I-PACE");
        JAGUAR_LIST.add("XE");
        JAGUAR_LIST.add("XF");
        JAGUAR_LIST.add("XJ");


        return JAGUAR_LIST;
    }

    //    MITSUBISHI_LIST CAR MODELS
    public List<String> mitsubhishiModels() {
        MITSUBISHI_LIST.add("Eclipse Cross");
        MITSUBISHI_LIST.add("Mirage");
        MITSUBISHI_LIST.add("Mirage G4");
        MITSUBISHI_LIST.add("Outlander");
        MITSUBISHI_LIST.add("Outlander PHEV");
        MITSUBISHI_LIST.add("Outlander Sport");


        return MITSUBISHI_LIST;
    }

    //    LAND_ROVER_LIST CAR MODELS
    public List<String> landRoverModels() {
        LAND_ROVER_LIST.add("Defender 110");
        LAND_ROVER_LIST.add("Defender 90");
        LAND_ROVER_LIST.add("Discovery");
        LAND_ROVER_LIST.add("Discovery Sport");
        LAND_ROVER_LIST.add("Range Rover");
        LAND_ROVER_LIST.add("Range Rover Evoque");
        LAND_ROVER_LIST.add("Range Rover Sport");
        LAND_ROVER_LIST.add("Range Rover Velar");


        return LAND_ROVER_LIST;
    }

    //    ASTON_MARTIN_LIST CAR MODELS
    public List<String> astonMartinModels() {
        ASTON_MARTIN_LIST.add("DB11");
        ASTON_MARTIN_LIST.add("DBS Superleggera");
        ASTON_MARTIN_LIST.add("Vantage");


        return ASTON_MARTIN_LIST;
    }

    //    MAZDA_LIST CAR MODELS
    public List<String> mazdaModels() {
        MAZDA_LIST.add("CX-3");
        MAZDA_LIST.add("CX-30");
        MAZDA_LIST.add("CX-5");
        MAZDA_LIST.add("CX-9");
        MAZDA_LIST.add("MAZDA3");
        MAZDA_LIST.add("MAZDA6");
        MAZDA_LIST.add("MX-5 Miata");
        MAZDA_LIST.add("MX-5 Miata RF");


        return MAZDA_LIST;
    }

    //    ROLLS_ROYCE_LIST CAR MODELS
    public List<String> rollsRoyceModels() {
        ROLLS_ROYCE_LIST.add("Cullinan");
        ROLLS_ROYCE_LIST.add("Dawn");
        ROLLS_ROYCE_LIST.add("Ghost");
        ROLLS_ROYCE_LIST.add("Phantom");
        ROLLS_ROYCE_LIST.add("Wraith");


        return ROLLS_ROYCE_LIST;
    }

    //   MINI CAR MODELS
    public List<String> miniModels() {
        MINI_LIST.add("Clubman");
        MINI_LIST.add("Countryman");
        MINI_LIST.add("Hardtop 2 Door");
        MINI_LIST.add("Hardtop 4 Door");


        return MINI_LIST;
    }


    //    JEEP CAR MODELS
    public List<String> jeepModels() {
        JEEP_LIST.add("Cherokee");
        JEEP_LIST.add("Compass");
        JEEP_LIST.add("Gladiator");
        JEEP_LIST.add("Grand Cherokee");
        JEEP_LIST.add("Renegade");
        JEEP_LIST.add("Wrangler");
        JEEP_LIST.add("Wrangler Unlimited");


        return JEEP_LIST;
    }

    //    DODGE CAR MODELS
    public List<String> dodgeModels() {
        DODGE_LIST.add("Challenger");
        DODGE_LIST.add("Charger");
        DODGE_LIST.add("Durango");
        DODGE_LIST.add("Grand Caravan Passenger");
        DODGE_LIST.add("Journey");


        return DODGE_LIST;
    }


    //    PORSCHE CAR MODELS
    public List<String> porscheModels() {
        PORSCHE_LIST.add("Cayenne");
        PORSCHE_LIST.add("Cayenne Coupé");
        PORSCHE_LIST.add("Macan");
        PORSCHE_LIST.add("Panamera");
        PORSCHE_LIST.add("Taycan");
        PORSCHE_LIST.add("718 Boxster");
        PORSCHE_LIST.add("718 Cayman");
        PORSCHE_LIST.add("718 Spyder");
        PORSCHE_LIST.add("911");

        return PORSCHE_LIST;
    }


    //    BUICK CAR MODELS
    public List<String> buickModels() {
        BUICK_LIST.add("Cascada");
        BUICK_LIST.add("Enclave");
        BUICK_LIST.add("Encore");
        BUICK_LIST.add("Encore GX");
        BUICK_LIST.add("Envision");
        BUICK_LIST.add("LaCrosse");
        BUICK_LIST.add("Regal Sportback");
        BUICK_LIST.add("Regal TourX");


        return BUICK_LIST;
    }


    //    KIA CAR MODELS
    public List<String> kiaModels() {
        KIA_LIST.add("Cadenza");
        KIA_LIST.add("Forte");
        KIA_LIST.add("K5");
        KIA_LIST.add("K900");
        KIA_LIST.add("Niro");
        KIA_LIST.add("Niro EV");
        KIA_LIST.add("Niro Plug-in Hybrid");
        KIA_LIST.add("Optima");
        KIA_LIST.add("Optima Hybrid");
        KIA_LIST.add("Optima Plug-in Hybrid");
        KIA_LIST.add("Rio");
        KIA_LIST.add("Sedona");
        KIA_LIST.add("Seltos");
        KIA_LIST.add("Sorento");
        KIA_LIST.add("Soul");
        KIA_LIST.add("Soul EV");
        KIA_LIST.add("Sportage");
        KIA_LIST.add("Stinger");
        KIA_LIST.add("Telluride");


        return KIA_LIST;
    }

    //    ford CAR MODELS
    public List<String> fordModels() {
        FORD_LIST.add("Bronco Sport");
        FORD_LIST.add("Corsair");
        FORD_LIST.add("EcoSport");
        FORD_LIST.add("Edge");
        FORD_LIST.add("Escape");
        FORD_LIST.add("Expedition");
        FORD_LIST.add("Expedition MAX");
        FORD_LIST.add("Explorer");
        FORD_LIST.add("F150 Regular Cab");
        FORD_LIST.add("F150 Super Cab");
        FORD_LIST.add("F150 SuperCrew Cab");
        FORD_LIST.add("F250 Super Duty Regular Cab");
        FORD_LIST.add("F250 Super Duty Super Cab");
        FORD_LIST.add("F350 Super Duty Crew Cab");
        FORD_LIST.add("F350 Super Duty Regular Cab");
        FORD_LIST.add("F350 Super Duty Super Cab");
        FORD_LIST.add("F450 Super Duty Crew Cab");
        FORD_LIST.add("F450 Super Duty Regular Cab");
        FORD_LIST.add("Fiesta");
        FORD_LIST.add("Flex");
        FORD_LIST.add("Fusion");
        FORD_LIST.add("Fusion Energi");
        FORD_LIST.add("Fusion Plug-in Hybrid");
        FORD_LIST.add("Fusion Plug-in Hybrid");
        FORD_LIST.add("Mustang");
        FORD_LIST.add("Ranger SuperCab");
        FORD_LIST.add("Ranger SuperCrew");
        FORD_LIST.add("Taurus");
        FORD_LIST.add("Transit 150 Cargo Van");
        FORD_LIST.add("Transit 150 Crew Van");
        FORD_LIST.add("Transit 150 Passenger Van");
        FORD_LIST.add("Transit 150 Van");
        FORD_LIST.add("Transit 150 Wagon");
        FORD_LIST.add("Transit 250 Cargo Van");
        FORD_LIST.add("Transit 250 Crew Van");
        FORD_LIST.add("Transit 250 Van");
        FORD_LIST.add("Transit 350 HD Van");
        FORD_LIST.add("Transit 350 Passenger Van");
        FORD_LIST.add("Transit 350 Van");
        FORD_LIST.add("Transit 350 Wagon");
        FORD_LIST.add("Transit Connect Cargo");
        FORD_LIST.add("Transit Connect Cargo Van");
        FORD_LIST.add("Transit Connect Passenger");
        FORD_LIST.add("Transit Connect Passenger Wagon");


        return FORD_LIST;

    }

    //    Chevrolet CAR MODELS
    public List<String> chevroletModels() {
        CHEVROLET_LIST.add("Blazer");
        CHEVROLET_LIST.add("Bolt EV");
        CHEVROLET_LIST.add("Camaro");
        CHEVROLET_LIST.add("Colorado Crew Cab");
        CHEVROLET_LIST.add("Colorado Extended Cab");
        CHEVROLET_LIST.add("Corvette");
        CHEVROLET_LIST.add("Cruze");
        CHEVROLET_LIST.add("Equinox");
        CHEVROLET_LIST.add("Express 2500 Cargo");
        CHEVROLET_LIST.add("Express 2500 Passenger");
        CHEVROLET_LIST.add("Express 3500 Cargo");
        CHEVROLET_LIST.add("Express 3500 Passenger");
        CHEVROLET_LIST.add("Impala");
        CHEVROLET_LIST.add("Malibu");
        CHEVROLET_LIST.add("Silverado 1500 Crew Cab");
        CHEVROLET_LIST.add("Silverado 1500 Double Cab");
        CHEVROLET_LIST.add("Silverado 1500 LD Double Cab");
        CHEVROLET_LIST.add("Silverado 1500 Regular Cab");
        CHEVROLET_LIST.add("Silverado 2500 HD Crew Cab");
        CHEVROLET_LIST.add("Silverado 2500 HD Double Cab");
        CHEVROLET_LIST.add("Silverado 2500 HD Regular Cab");
        CHEVROLET_LIST.add("Silverado 3500 HD Crew Cab");
        CHEVROLET_LIST.add("Sonic");
        CHEVROLET_LIST.add("Spark");
        CHEVROLET_LIST.add("Suburban");
        CHEVROLET_LIST.add("Tahoe");
        CHEVROLET_LIST.add("Trailblazer");
        CHEVROLET_LIST.add("Traverse");
        CHEVROLET_LIST.add("Trax");
        CHEVROLET_LIST.add("Volt");


        return CHEVROLET_LIST;
    }


    //    BENTLEY CAR MODELS
    public List<String> bentleyModels() {
        BENTLEY_LIST.add("Bentayga");
        BENTLEY_LIST.add("Mulsanne");


        return BENTLEY_LIST;
    }

    //    Lincoln CAR MODELS

    public List<String> lincolnModels() {
        LINCOLN_LIST.add("Aviator");
        LINCOLN_LIST.add("Continental");
        LINCOLN_LIST.add("MKC");
        LINCOLN_LIST.add("MKT");
        LINCOLN_LIST.add("MKZ");
        LINCOLN_LIST.add("Nautilus");
        LINCOLN_LIST.add("Navigator");
        LINCOLN_LIST.add("Navigator L");


        return LINCOLN_LIST;
    }


//    Lamborghini CAR MODELS

    public List<String> lamborghiniModels() {
        LAMBORGHINI_LIST.add("Aventador");
        LAMBORGHINI_LIST.add("Huracan");
        LAMBORGHINI_LIST.add("Urus");

        return LAMBORGHINI_LIST;
    }

    //    CADILLAC CAR MODELS

    public List<String> cadillacModels() {
        CADILLAC_LIST.add("ATS");
        CADILLAC_LIST.add("ATS-V");
        CADILLAC_LIST.add("CT4");
        CADILLAC_LIST.add("CT5");
        CADILLAC_LIST.add("CT6");
        CADILLAC_LIST.add("CT6-V");
        CADILLAC_LIST.add("CTS");
        CADILLAC_LIST.add("CTS-V");
        CADILLAC_LIST.add("Escalade");
        CADILLAC_LIST.add("Escalade ESV");
        CADILLAC_LIST.add("XT4");
        CADILLAC_LIST.add("XT5");
        CADILLAC_LIST.add("XT6");
        CADILLAC_LIST.add("XTS");


        return CADILLAC_LIST;
    }


    //    TOYOTA CAR MODELS

    public List<String> toyotaModels() {
        TOYOTA_LIST.add("Avalon");
        TOYOTA_LIST.add("Avalon Hybrid");
        TOYOTA_LIST.add("Camry");
        TOYOTA_LIST.add("Camry Hybrid");
        TOYOTA_LIST.add("C-HR");
        TOYOTA_LIST.add("Corolla");
        TOYOTA_LIST.add("Corolla Hatchback");
        TOYOTA_LIST.add("Corolla Hybrid");
        TOYOTA_LIST.add("GR Supra");
        TOYOTA_LIST.add("Highlander");
        TOYOTA_LIST.add("Highlander Hybrid");
        TOYOTA_LIST.add("Land Cruiser");
        TOYOTA_LIST.add("Mirai");
        TOYOTA_LIST.add("Prius");
        TOYOTA_LIST.add("Prius c");
        TOYOTA_LIST.add("Prius Prime");
        TOYOTA_LIST.add("RAV4");
        TOYOTA_LIST.add("RAV4 Hybrid");
        TOYOTA_LIST.add("RAV4 Prime");
        TOYOTA_LIST.add("Sequoia");
        TOYOTA_LIST.add("Sienna");
        TOYOTA_LIST.add("Tacoma Access Cab");
        TOYOTA_LIST.add("Tacoma Double Cab");
        TOYOTA_LIST.add("Tundra CrewMax");
        TOYOTA_LIST.add("Tundra Double Cab");
        TOYOTA_LIST.add("Venza");
        TOYOTA_LIST.add("Versa");
        TOYOTA_LIST.add("Yaris");
        TOYOTA_LIST.add("Yaris Hatchback");
        TOYOTA_LIST.add("4Runner");
        TOYOTA_LIST.add("86");

        return TOYOTA_LIST;
    }


    //    SUBARU CAR MODELS

    public List<String> subaruModels() {
        SUBARU_LIST.add("Ascent");
        SUBARU_LIST.add("BRZ");
        SUBARU_LIST.add("Crosstrek");
        SUBARU_LIST.add("Forester");
        SUBARU_LIST.add("Impreza");
        SUBARU_LIST.add("Legacy");
        SUBARU_LIST.add("Outback");
        SUBARU_LIST.add("WRX");


        return SUBARU_LIST;
    }

    //    VOLKSWAGEN CAR MODELS

    public List<String> volkswagenModels() {
        VOLKSWAGEN_LIST.add("Arteon");
        VOLKSWAGEN_LIST.add("Atlas");
        VOLKSWAGEN_LIST.add("Atlas Cross Sport");
        VOLKSWAGEN_LIST.add("Beetle");
        VOLKSWAGEN_LIST.add("e-Golf");
        VOLKSWAGEN_LIST.add("Golf");
        VOLKSWAGEN_LIST.add("Golf Alltrack");
        VOLKSWAGEN_LIST.add("Golf GTI");
        VOLKSWAGEN_LIST.add("Golf R");
        VOLKSWAGEN_LIST.add("Golf SportWagen");
        VOLKSWAGEN_LIST.add("Jetta");
        VOLKSWAGEN_LIST.add("Jetta GLI");
        VOLKSWAGEN_LIST.add("Passat");
        VOLKSWAGEN_LIST.add("Tiguan");


        return VOLKSWAGEN_LIST;
    }

    //    AUDI CAR MODELS

    public List<String> audiModels() {
        AUDI_LIST.add("A3");
        AUDI_LIST.add("A4");
        AUDI_LIST.add("A4 allroad");
        AUDI_LIST.add("A5");
        AUDI_LIST.add("A6");
        AUDI_LIST.add("A6 allroad");
        AUDI_LIST.add("A7");
        AUDI_LIST.add("A8");
        AUDI_LIST.add("e-tron");
        AUDI_LIST.add("Q3");
        AUDI_LIST.add("Q5");
        AUDI_LIST.add("Q50");
        AUDI_LIST.add("Q60");
        AUDI_LIST.add("Q7");
        AUDI_LIST.add("Q70");
        AUDI_LIST.add("Q8");
        AUDI_LIST.add("R8");
        AUDI_LIST.add("RS 3");
        AUDI_LIST.add("RS 5");
        AUDI_LIST.add("S3");
        AUDI_LIST.add("S4");
        AUDI_LIST.add("S5");
        AUDI_LIST.add("S8");
        AUDI_LIST.add("SQ5");
        AUDI_LIST.add("TT");


        return AUDI_LIST;
    }

//    NISSAN CAR MODELS

    public List<String> nissanModels() {
        NISSAN_LIST.add("Altima");
        NISSAN_LIST.add("Armada");
        NISSAN_LIST.add("Frontier Crew Cab");
        NISSAN_LIST.add("Frontier King Cab");
        NISSAN_LIST.add("GT-R");
        NISSAN_LIST.add("Kicks");
        NISSAN_LIST.add("LEAF");
        NISSAN_LIST.add("Maxima");
        NISSAN_LIST.add("Murano");
        NISSAN_LIST.add("NV1500 Cargo");
        NISSAN_LIST.add("NV200");
        NISSAN_LIST.add("NV2500 HD Cargo");
        NISSAN_LIST.add("NV3500 HD Cargo");
        NISSAN_LIST.add("NV3500 HD Passenger");
        NISSAN_LIST.add("Pathfinder");
        NISSAN_LIST.add("Rogue");
        NISSAN_LIST.add("Rogue Sport");
        NISSAN_LIST.add("Sentra");
        NISSAN_LIST.add("Titan Crew Cab");
        NISSAN_LIST.add("Titan King Cab");
        NISSAN_LIST.add("TITAN Single Cab");
        NISSAN_LIST.add("TITAN XD Crew Cab");
        NISSAN_LIST.add("TITAN XD King Cab");
        NISSAN_LIST.add("TITAN XD Single Cab");
        NISSAN_LIST.add("Versa");
        NISSAN_LIST.add("Versa Note");
        NISSAN_LIST.add("370Z");


        return NISSAN_LIST;
    }

//    HYUNDAI CAR MODELS

    public List<String> hyundaiModels() {
        HYUNDAI_LIST.add("Accent");
        HYUNDAI_LIST.add("Elantra");
        HYUNDAI_LIST.add("Elantra GT");
        HYUNDAI_LIST.add("Ioniq Electric");
        HYUNDAI_LIST.add("Ioniq Hybrid");
        HYUNDAI_LIST.add("Ioniq Plug-in Hybrid");
        HYUNDAI_LIST.add("Kona");
        HYUNDAI_LIST.add("Kona Electric");
        HYUNDAI_LIST.add("NEXO");
        HYUNDAI_LIST.add("Palisade");
        HYUNDAI_LIST.add("Santa Fe");
        HYUNDAI_LIST.add("Santa Fe XL");
        HYUNDAI_LIST.add("Sonata");
        HYUNDAI_LIST.add("Sonata Hybrid");
        HYUNDAI_LIST.add("Sonata Plug-in Hybrid");
        HYUNDAI_LIST.add("Tucson");
        HYUNDAI_LIST.add("Veloster");
        HYUNDAI_LIST.add("Venue");


        return HYUNDAI_LIST;

    }
//    MERCEDES-BENZ CAR MODELS

    public List<String> mercedesBenzModels() {
        MERCEDES_LIST.add("A-Class");
        MERCEDES_LIST.add("C-Class");
        MERCEDES_LIST.add("CLA");
        MERCEDES_LIST.add("CLS");
        MERCEDES_LIST.add("E-Class");
        MERCEDES_LIST.add("G-Class");
        MERCEDES_LIST.add("GLA");
        MERCEDES_LIST.add("GLB");
        MERCEDES_LIST.add("GLC");
        MERCEDES_LIST.add("GLC Coupe");
        MERCEDES_LIST.add("GLE");
        MERCEDES_LIST.add("GLS");
        MERCEDES_LIST.add("Mercedes-AMG A-Class");
        MERCEDES_LIST.add("Mercedes-AMG C-Class");
        MERCEDES_LIST.add("Mercedes-AMG CLA");
        MERCEDES_LIST.add("Mercedes-AMG CLS");
        MERCEDES_LIST.add("Mercedes-AMG E-Class");
        MERCEDES_LIST.add("Mercedes-AMG G-Class");
        MERCEDES_LIST.add("Mercedes-AMG GLA");
        MERCEDES_LIST.add("Mercedes-AMG GLC");
        MERCEDES_LIST.add("Mercedes-AMG GLC Coupe");
        MERCEDES_LIST.add("Mercedes-AMG GLE");
        MERCEDES_LIST.add("Mercedes-AMG GLE Coupe");
        MERCEDES_LIST.add("Mercedes-AMG GLS");
        MERCEDES_LIST.add("Mercedes-AMG GT");
        MERCEDES_LIST.add("Mercedes-AMG S-Class");
        MERCEDES_LIST.add("Mercedes-AMG SL");
        MERCEDES_LIST.add("Mercedes-AMG SLC");
        MERCEDES_LIST.add("Mercedes-Maybach S-Class");
        MERCEDES_LIST.add("Metris Cargo");
        MERCEDES_LIST.add("Metris Passenger");
        MERCEDES_LIST.add("Metris WORKER Cargo");
        MERCEDES_LIST.add("Metris WORKER Passenger");
        MERCEDES_LIST.add("S-Class");
        MERCEDES_LIST.add("SL");
        MERCEDES_LIST.add("SLC");
        MERCEDES_LIST.add("Sprinter 1500 Cargo");
        MERCEDES_LIST.add("Sprinter 1500 Passenger");
        MERCEDES_LIST.add("Sprinter 2500 Cargo");
        MERCEDES_LIST.add("Sprinter 2500 Crew");
        MERCEDES_LIST.add("Sprinter 2500 Passenger");
        MERCEDES_LIST.add("Sprinter 3500 Cargo");
        MERCEDES_LIST.add("Sprinter 3500 Crew");
        MERCEDES_LIST.add("Sprinter 3500 XD Cargo");
        MERCEDES_LIST.add("Sprinter 3500 XD Crew");
        MERCEDES_LIST.add("Sprinter 3500XD Cargo");
        MERCEDES_LIST.add("Sprinter 4500 Cargo");
        MERCEDES_LIST.add("Sprinter 4500 Crew");


        return MERCEDES_LIST;
    }
//    HONDA CAR MODELS

    public List<String> hondaModels() {
        HONDA_LIST.add("Accord");
        HONDA_LIST.add("Accord Hybrid");
        HONDA_LIST.add("Civic");
        HONDA_LIST.add("Civic Type R");
        HONDA_LIST.add("Clarity Electric");
        HONDA_LIST.add("Clarity Fuel Cell");
        HONDA_LIST.add("Clarity Plug-in Hybrid");
        HONDA_LIST.add("CR-V");
        HONDA_LIST.add("CR-V Hybrid");
        HONDA_LIST.add("Fit");
        HONDA_LIST.add("HR-V");
        HONDA_LIST.add("Insight");
        HONDA_LIST.add("NSX");
        HONDA_LIST.add("Odyssey");
        HONDA_LIST.add("Passport");
        HONDA_LIST.add("Pilot");
        HONDA_LIST.add("Ridgeline");


        return HONDA_LIST;
    }

    //    GMC CAR MODELS
    public List<String> gmcModels() {
        GMC_LIST.add("Acadia");
        GMC_LIST.add("Canyon Crew Cab");
        GMC_LIST.add("Canyon Extended Cab");
        GMC_LIST.add("Savana 2500 Cargo");
        GMC_LIST.add("Savana 2500 Passenger");
        GMC_LIST.add("Savana 3500 Cargo");
        GMC_LIST.add("Savana 3500 Passenger");
        GMC_LIST.add("Sierra 1500 Crew Cab");
        GMC_LIST.add("Sierra 1500 Double Cab");
        GMC_LIST.add("Sierra 1500 Limited Double Cab");
        GMC_LIST.add("Sierra 2500 HD Crew Cab");
        GMC_LIST.add("Sierra 2500 HD Double Cab");
        GMC_LIST.add("Sierra 2500 HD Regular Cab");
        GMC_LIST.add("Sierra 3500 HD Crew Cab");
        GMC_LIST.add("Terrain");
        GMC_LIST.add("Yukon");
        GMC_LIST.add("Yukon XL");

        return GMC_LIST;
    }
}

