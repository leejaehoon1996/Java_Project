package Java_project_1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends Frame implements ActionListener{

		Font font17 = new Font("TimesRoman", Font.BOLD, 17);
		Font font15 = new Font("TimesRoman", Font.BOLD, 15);
		Font font13 = new Font("TimesRoman", Font.BOLD, 13);
		Font font12b = new Font("TimesRoman", Font.BOLD, 12);
		
		
		Label Title = new Label("관리자 모드");
		
		Button book  = new Button("도서관리");
		Button member = new Button("회원관리");
		Button close  = new Button("닫기");

		Main(){
			super("도서회원관리 프로그램");
			this.setSize(300, 300);
			this.setLocation(700, 350);
			initi();
			start();
			this.setVisible(true);
			
		}
		
		void initi() {
			this.setLayout(null);
			this.add(Title); this.add(book); this.add(member);  this.add(close);

			Title.setFont(font17);
			
			Title.setBounds(100, 50, 200, 30); book.setBounds(100, 100, 100, 30);
			member.setBounds(100, 150, 100, 30); close.setBounds(110, 200, 80, 30);
	}
		void start() {
			book.addActionListener(this); member.addActionListener(this); close.addActionListener(this); 
			
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==book) {Bookmain book = new Bookmain();}
			else if(e.getSource()==member) {ManageMain member = new ManageMain();}
			else if(e.getSource()==close) {System.exit(0);}
		}
}

