/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

/**
 *
 * @author LBUK
 */
public class AnnosRaakaAine {
    private Integer id;
    private Integer annosId;
    private Integer raakaAineId;
    private Integer jarjestys;
    private Integer maara;
    private String ohje;
    
    public AnnosRaakaAine(Integer id, Integer annosId, Integer raakaAineId, Integer jarjestys, Integer maara, String ohje) {
        this.id = id;
        this.annosId = annosId;
        this.raakaAineId = raakaAineId;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
    }

    public Integer getId() {
        return id;
    }

    public Integer getAnnosId() {
        return annosId;
    }

    public Integer getRaakaAineId() {
        return raakaAineId;
    }

    public Integer getJarjestys() {
        return jarjestys;
    }

    public Integer getMaara() {
        return maara;
    }

    public String getOhje() {
        return ohje;
    }

    public void setMaara(Integer lisays) {
        this.maara += lisays;
    }
    
    
    
    
}