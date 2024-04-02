package com.felipe.uniroom.config;

import java.awt.*;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Constants {
    //Texts
    public static final String UNIROOM = "UniRoom";
    public static final String REGISTER = "Registrar";
    public static final String SUCCESSFUL_REGISTER = "Registrado com sucesso!";
    public static final String FAILED_REGISTER = "Registrar falhou. Tente novamente.";
    public static final String SUCCESSFUL_LOGIN = "Entrou com sucesso!";
    public static final String FAILED_LOGIN = "Login falhou. Tente novamente.";
    public static final String LOGIN = "Login";
    public static final String USER = "Usuário";
    public static final String PASSWORD = "Senha";
    public static final String ERROR = "Erro";
    public static final String EDIT = "Editar";
    public static final String DELETE = "Apagar";
    public static final String NAME = "Nome";
    public static final String NUMBER = "Número do quarto";
    public static final String FORGOT = "Esqueci minha senha";
    public static final String SECRET_PHRASE = "Pergunta secreta";
    public static final String SECRET_ANSWER = "Sua resposta";
    public static final String SEND = "Enviar";
    public static final String NEW_RESERVATION = "Nova reserva";
    public static final String GUEST = "Hóspede";
    public static final String ADD_GUEST = "Adicionar hóspede";
    public static final String BACK = "Voltar";
    public static final String RESERVATIONS_HISTORY = "Histórico de reservas";
    public static final String CORPORATE = "Matriz";
    public static final String BRANCH = "Filial";
    public static final String INVENTORY = "Inventário";
    public static final String ITEM = "Item";
    public static final String RESERVATION = "Reserva";
    public static final String ROOM = "Quarto";
    public static final String ROOM_TYPE = "Tipo de quarto";

    //Colors
    public static final Color WHITE = new Color(0xFFFFFF);
    public static final Color BLACK = new Color(0x000000);
    public static final Color GREEN = new Color(0x096B06);
    public static final Color BLUE = new Color(18, 52, 86);
    public static final Color RED = new Color(0xBE0606);
    public static final Color GRAY = new Color(0xCCCCCC);

    //Fonts
    public static final Font FONT = new Font("Sans", Font.PLAIN, 20);

    //Sizes
    public static final Dimension BUTTON_SIZE = new Dimension(300, 40);
    public static final Dimension TEXT_FIELD_SIZE = new Dimension(300, 10);

    //Icons
    public static final Icon USER_ICON = new ImageIcon("src/main/resources/images/icons/user.png");
    public static final Icon BRANCH_ICON = new ImageIcon("src/main/resources/images/icons/user.png");
    public static final Icon CORPORATE_ICON = new ImageIcon("src/main/resources/images/icons/corporate.png");
    public static final Icon INVENTORY_ICON = new ImageIcon("src/main/resources/images/icons/inventory.png");
    public static final Icon ITEM_ICON = new ImageIcon("src/main/resources/images/icons/item.png");
    public static final Icon RESERVATION_ICON = new ImageIcon("src/main/resources/images/icons/reservation.png");
    public static final Icon ROOM_TYPE_ICON = new ImageIcon("src/main/resources/images/icons/user.png");
    public static final Icon ROOM_ICON = new ImageIcon("src/main/resources/images/icons/room.png");
}
