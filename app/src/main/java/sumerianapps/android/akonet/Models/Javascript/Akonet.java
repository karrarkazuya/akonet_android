package sumerianapps.android.akonet.Models.Javascript;

public class Akonet {

    public static String makeJsonMain(){
        return "var api = document.getElementsByTagName(\"pre\")[0].textContent;";
    }

    public static String getISP(){
        return "var isp = document.getElementsByClassName(\"aka\")[0].textContent; isp = isp.substring(isp.indexOf(\"'\")+1,isp.length-1);";
    }
}
