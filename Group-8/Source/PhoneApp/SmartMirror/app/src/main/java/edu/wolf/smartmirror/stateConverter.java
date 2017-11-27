package edu.wolf.smartmirror;

public class stateConverter {

    int id;
    int relativeId;

    public stateConverter(String state){
        id = 0;
        relativeId = -1;

        if(state == null)
        {
            return;
        }

        switch(state) {
            case "DC" :
                id = 21140;
                relativeId = 0;
                break;
            case "Alabama" :
                id = 21133;
                relativeId = 1;
                break;
            case "Alaska" :
                id = 21132;
                relativeId = 2;
                break;
            case "Arizona" :
                id = 21136;
                relativeId = 3;
                break;
            case "Arkansas" :
                id = 21135;
                relativeId = 4;
                break;
            case "California" :
                id = 21137;
                relativeId = 5;
                break;
            case "Colorado" :
                id = 21138;
                relativeId = 6;
                break;
            case "Connecticut" :
                id = 21139;
                relativeId = 7;
                break;
            case "Delaware" :
                id = 21141;
                relativeId = 8;
                break;
            case "Florida" :
                id = 21142;
                relativeId = 9;
                break;
            case "Georgia" :
                id = 21143;
                relativeId = 10;
                break;
            case "Hawaii" :
                id = 21144;
                relativeId = 11;
                break;
            case "Idaho" :
                id = 21146;
                relativeId = 12;
                break;
            case "Illinois" :
                id = 21147;
                relativeId = 13;
                break;
            case "Indiana" :
                id = 21148;
                relativeId = 14;
                break;
            case "Iowa" :
                id = 21145;
                relativeId = 15;
                break;
            case "Kansas" :
                id = 21149;
                relativeId = 16;
                break;
            case "Kentucky" :
                id = 21150;
                relativeId = 17;
                break;
            case "Louisiana" :
                id = 21151;
                relativeId = 18;
                break;
            case "Maine" :
                id = 21154;
                relativeId = 19;
                break;
            case "Maryland" :
                id = 21153;
                relativeId = 20;
                break;
            case "Massachusetts" :
                id = 21152;
                relativeId = 21;
                break;
            case "Michigan" :
                id = 21155;
                relativeId = 22;
                break;
            case "Minnesota" :
                id = 21156;
                relativeId = 23;
                break;
            case "Mississippi" :
                id = 21158;
                relativeId = 24;
                break;
            case "Missouri" :
                id = 21157;
                relativeId = 25;
                break;
            case "Montana" :
                id = 21159;
                relativeId = 26;
                break;
            case "Nebraska" :
                id = 21162;
                relativeId = 27;
                break;
            case "Nevada" :
                id = 21166;
                relativeId = 28;
                break;
            case "New Hampshire" :
                id = 21163;
                relativeId = 29;
                break;
            case "New Jersey" :
                id = 21164;
                relativeId = 30;
                break;
            case "New Mexico" :
                id = 21165;
                relativeId = 31;
                break;
            case "New York" :
                id = 21167;
                relativeId = 32;
                break;
            case "North Carolina" :
                id = 21160;
                relativeId = 33;
                break;
            case "North Dakota" :
                id = 21161;
                relativeId = 34;
                break;
            case "Ohio" :
                id = 21168;
                relativeId = 35;
                break;
            case "Oklahoma" :
                id = 21169;
                relativeId = 36;
                break;
            case "Oregon" :
                id = 21170;
                relativeId = 37;
                break;
            case "Pennsylvania" :
                id = 21171;
                relativeId = 38;
                break;
            case "Rhode Island" :
                id = 21172;
                relativeId = 39;
                break;
            case "South Carolina" :
                id = 21173;
                relativeId = 40;
                break;
            case "South Dakota" :
                id = 21174;
                relativeId = 41;
                break;
            case "Tennessee" :
                id = 21175;
                relativeId = 42;
                break;
            case "Texas" :
                id = 21176;
                relativeId = 43;
                break;
            case "Utah" :
                id = 21177;
                relativeId = 44;
                break;
            case "Vermont" :
                id = 21179;
                relativeId = 45;
                break;
            case "Virginia" :
                id = 21178;
                relativeId = 46;
                break;
            case "Washington" :
                id = 21180;
                relativeId = 47;
                break;
            case "West Virginia" :
                id = 21183;
                relativeId = 48;
                break;
            case "Wisconsin" :
                id = 21182;
                relativeId = 49;
                break;
            case "Wyoming" :
                id = 21184;
                relativeId = 50;
                break;
            default :
                id = 0;
                relativeId = -1;
        }
    }

    public String getId(){
        return Integer.toString(id);
    }

    public int getRelativeId() {
        return relativeId;
    }

}
