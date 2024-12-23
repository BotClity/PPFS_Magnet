// PPFS_Magnet Plugin
// Авторские права (c) 2024 PPFSS
// Лицензия: MIT
package com.ppfs.Models;

import com.ppfs.PPFS_Magnet;
import com.ppfs.ppfs_libs.models.configs.ConfigJSON;
import com.ppfs.ppfs_libs.models.message.Message;
import lombok.Getter;

@Getter
public class Messages extends ConfigJSON {
    private static Messages instance;

    public static Messages getInstance(){
        if (instance == null)instance = load(PPFS_Magnet.getInstance(), "messages", Messages.class);
        return instance;
    }


    Message no_permission = new Message("<dark_green>[PPFS_Magnet]<red>У Вас недостаточно прав!");
    Message magnet_give_successfully = new Message("<dark_green>[PPFS_Magnet]<green>Магнит успешно выдан.</green>(Радиус работы: <radius>)");
    Message no_item_in_right_hand = new Message("<dark_green>[PPFS_Magnet]<red>У Вас нету предмета в правой руке.");
    Message already_magnet = new Message("<dark_green>[PPFS_Magnet]<red>Предмет уже обладает свойствами магнита.");
    Message magnet_cast_successfully = new Message("<dark_green>[PPFS_Magnet]<green>Предмет успешно приобрел свойства магнита");
    Message item_not_magnet = new Message("<dark_green>[PPFS_Magnet]<red>Ваш предмет не является магнитом!");
    Message magnet_uncast_successfully = new Message("<dark_green>[PPFS_Magnet]<green>Предмет больше не магнит");
    Message not_number = new Message("<<dark_green>[PPFS_Magnet]red>Вы ввели не число");
    Message no_args = new Message("<dark_green>[PPFS_Magnet]<red>недостаточно аргументов");
    Message radius_updated = new Message("<dark_green>[PPFS_Magnet]<green>Радиус обновлен.");
    Message magnet_activated = new Message("<dark_green>[PPFS_Magnet]<green>Магнит активирован.");
    Message magnet_deactivated = new Message("<dark_green>[PPFS_Magnet]<red>Магнит отключён.");
}
