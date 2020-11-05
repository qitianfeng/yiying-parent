package com.yiying.movie.dto;

import lombok.Data;

@Data
public class PlayHallSeat {
    //座位的列
    private String seatsColumn;

    // 座位的行
    private String seatsRow;
    //座位的状态  1 表示已经被占， 0 表示未被占
    private String status;

    @Override
    public String toString() {
        return "PlayHallSeat{" +
                ", seatsRow='" + seatsRow + '\'' +
                "seatsColumn='" + seatsColumn + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
