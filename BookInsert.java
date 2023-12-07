package Java_project_1;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookInsert extends Frame implements ActionListener, ItemListener{

	String url = "jdbc:mysql://localhost:3306/java_db?useUnicode=true&characterEncoding=utf8";
	String id = "root";
	String pass = "qwer";
	private Connection dc;
	
	Font font20 = new Font("TimesRoman", Font.BOLD, 20);
	Font font15 = new Font("TimesRoman", Font.BOLD, 15);
	Font font13 = new Font("TimesRoman", Font.BOLD, 13);
	Font font12b = new Font("TimesRoman", Font.BOLD, 12);
	
	
	Label Title = new Label("도서추가");
	Label name = new Label("도서명 : ");
	TextField namet = new TextField();
	Label writer = new Label("저자 : ");
	TextField writert = new TextField();
	Label publish = new Label("출판사 : ");
	TextField publisht = new TextField();
	Label cord = new Label("분류코드 : ");
	Choice cordt = new Choice();
	Label stack = new Label("재고 : ");
	Choice stackt = new Choice();
	Button register = new Button("추가하기");
	Button close  = new Button("닫기");
	
	
	BookInsert(){
		super("도서관리 프로그램");
		this.setSize(300, 450);
		this.setLocation(700, 350);
		initi();
		start();
		this.setVisible(true);
		
		try {
			Class.forName("org.gjt.mm.mysql.Driver");
		} catch (ClassNotFoundException ee) {
			return;
		}
		try {
			dc = DriverManager.getConnection(url, id, pass);
		} catch (SQLException ee) {
		}
	}
	
	
	void initi() {
		this.setLayout(null);
		this.add(Title);
		this.add(name); this.add(namet); this.add(writer); this.add(writert); this.add(publish); this.add(publisht); 
		this.add(stack); this.add(stackt);  this.add(cord); this.add(cordt); this.add(register); this.add(close);
		
		
		Title.setFont(font15); name.setFont(font13); writer.setFont(font13); stack.setFont(font13); publish.setFont(font13); cord.setFont(font13);
		
		cordt.add("[0]총류"); cordt.add("[100]철학"); cordt.add("[200]종교"); cordt.add("[300]사회과학"); cordt.add("[400]순수과학");
		cordt.add("[500]기술과학"); cordt.add("[600]예술"); cordt.add("[700]언어"); cordt.add("[800]문학"); cordt.add("[900]역사");
		
		stackt.add("대여가능"); stackt.add("대여중");
		
		Title.setBounds(110, 50, 200, 30);
		name.setBounds(65, 100, 60, 30); namet.setBounds(140, 100, 90, 30);
		writer.setBounds(75, 150, 50, 30); writert.setBounds(140, 150, 90, 30);
		publish.setBounds(65, 200, 70, 30); publisht.setBounds(140, 200, 90, 30);
		cord.setBounds(50, 250, 70, 30); cordt.setBounds(140, 250, 90, 30);
		stack.setBounds(70, 290, 70, 30); stackt.setBounds(140, 290, 90, 30);
		register.setBounds(110, 350, 80, 30);
		close.setBounds(120, 390, 60, 30);
	}
	
	void close() {this.setVisible(false); new Bookmain();}
	
	void start() {
		register.addActionListener(this); close.addActionListener(this); cordt.addItemListener(this);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==register) {
			String name = namet.getText();
			String writer = writert.getText();
			String publish = publisht.getText();
			String temp = cordt.getSelectedItem();
			String stack = stackt.getSelectedItem();
	
			if(name.equals("")) {msgDlg("도서명을 입력하세요"); return;}
			if(writer.equals("")) {msgDlg("저자을 입력하세요"); return;}
			if(publish.equals("")) {msgDlg("출판사를 입력하세요"); return;}
			if(temp.equals("")) {msgDlg("분류코드을 선택하세요"); return;}
			if(stack.equals("")) {msgDlg("분류코드을 선택하세요"); return;}
			
			//DB처리
			Insert(name, writer, publish, temp , stack);
			
			msgDlg("추가되었습니다");
			namet.setText(""); writert.setText(""); publisht.setText(""); 
		}
		else if(e.getSource()==close) {close();}
	}
	
	void Insert(String name, String writer, String publish, String temp, String stack) {
		String query = "insert into tb_javaproject1 values(null, ?, ?, ?, ?, ?)";
		
		try {
			PreparedStatement pstmt = dc.prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.setString(2, writer);
			pstmt.setString(3, publish);
			pstmt.setString(4, temp);
			pstmt.setString(5, stack);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException ee) {
			System.err.println("회원 가입 실패!! : " + ee.toString());
			
		}
	}
	
	void msgDlg(String msg)
	{
		//다이얼로그창
    	Dialog dlg = new Dialog(this, "알림창", true);
    	//컴포넌트선언
    	Button bt = new Button("확인");
    	Label lb = new Label(msg);
    	//레이아웃설정
		dlg.setLayout(null);
		//컴포넌트 다이얼로그에 등록
		dlg.add(bt); dlg.add(lb);
	
		//배치하기   		
		lb.setBounds(110, 70, 150, 30);
		bt.setBounds(140, 120, 100, 30);
		
		//이벤트등록
		bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dlg.setVisible(false);
			}
		});	
		//이벤트등록
		dlg.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dlg.setVisible(false);
			}
		});
		dlg.setLocation(650, 400);
		dlg.setSize(400, 200);
		dlg.setVisible(true);
	}


	@Override
	public void itemStateChanged(ItemEvent e) {
		
	}
	
}
