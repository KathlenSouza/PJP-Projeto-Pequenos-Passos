package com.api.pjpPequenosPassos.dto;

import java.util.List;

public class RadarDTO {

    public static class Item {
        public String area;
        public int percentual;

        public Item(String area, int percentual) {
            this.area = area;
            this.percentual = percentual;
        }
    }

    private List<Item> progresso;

    public RadarDTO(List<Item> progresso) {
        this.progresso = progresso;
    }

    public List<Item> getProgresso() {
        return progresso;
    }
}
