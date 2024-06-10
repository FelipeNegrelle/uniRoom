package com.felipe.uniroom.config;

import com.felipe.uniroom.Main;

import java.awt.*;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Constants {
    //Texts
    public static final String UNIROOM = "UniRoom";
    public static final String REGISTER = "Registrar";
    public static final String ACTIONS = "Ações";
    public static final String SUCCESSFUL_REGISTER = "Registrado com sucesso";
    public static final String FAILED_REGISTER = "Registrar falhou. Tente novamente";
    public static final String SUCCESSFUL_LOGIN = "Entrou com sucesso";
    public static final String SUCCESS = "Sucesso";
    public static final String FAILED_LOGIN = "Login falhou, tente novamente";
    public static final String GENERIC_ERROR = "Um erro ocorreu, tente novamente";
    public static final String LOGIN = "Login";
    public static final String USER = "Usuário";
    public static final String PASSWORD = "Senha";
    public static final String CONFIRM_PASSWORD = "Confimar Senha";
    public static final String ERROR = "Erro";
    public static final String EDIT = "Editar";
    public static final String SAVE = "Salvar";
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
    public static final String ITEMS = "Itens";
    public static final String RESERVATION = "Reserva";
    public static final String ROOM = "Quarto";
    public static final String ROOM_TYPE = "Tipo de quarto";
    public static final String EDIT_WARN = "Selecione um item para editar";
    public static final String DELETE_WARN = "Selecione um item para excluir";
    public static final String WARN = "Aviso";
    public static final String NEW = "Novo";
    public static final String SEARCH = "Pesquisa";
    public static final String CNPJ = "CNPJ";
    public static final String CPF = "CPF";
    public static final String EXIT = "Sair";
    public static final String HOSTED = "Hospedado";
    public static final String RECOVER_PASSWORD = "Recuperar Senha";
    public static final String QUANTITY = "Quantidade";

    //Colors
    public static final Color WHITE = new Color(0xFFFFFF);
    public static final Color BLACK = new Color(0x000000);
    public static final Color GREEN = new Color(0x096B06);
    public static final Color BLUE = new Color(0x123456);
    public static final Color RED = new Color(0xBE0606);
    public static final Color GRAY = new Color(0xCCCCCC);
    public static final Color BROWN = new Color(0x563412);
    public static final Color YELLOW = new Color(0xE49E00);

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
    public static final Icon CHECKBOX_ICON = new ImageIcon("src/main/resources/images/icons/checkbox.png");
    public static final Icon BOX_ICON = new ImageIcon("src/main/resources/images/icons/box.png");
    public static final Icon MORE_ICON = new ImageIcon("src/main/resources/images/icons/more.png");
    public static final Icon EDIT_ICON = new ImageIcon("src/main/resources/images/icons/edit.png");
    public static final Icon DELETE_ICON = new ImageIcon("src/main/resources/images/icons/delete.png");
    public static final Icon BACK_ICON = new ImageIcon("src/main/resources/images/icons/arrow-back.png");
    public static final Icon ADD_ICON = new ImageIcon("src/main/resources/images/icons/add.png");
    public static final Icon EXIT_ICON = new ImageIcon("src/main/resources/images/icons/exit.png");
    public static final Image LOGO = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/images/uniroom.jpeg"));
}
