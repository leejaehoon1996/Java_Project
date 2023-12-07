package Java_project_1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Managedelet extends Frame implements ActionListener{
	
	String url = "jdbc:mysql://localhost:3306/java_db?useUnicode=true&characterEncoding=utf8";
	String id = "root";
	String pass = "qwer";
	private Connection dc;
	
	Font font20 = new Font("TimesRoman", Font.BOLD, 20);
	Font font15 = new Font("TimesRoman", Font.BOLD, 15);
	Font font13 = new Font("TimesRoman", Font.BOLD, 13);
	Font font12b = new Font("TimesRoman", Font.BOLD, 12);
	
	Label Title = new Label("고객 삭제");
	Label Title2 = new Label("삭제할 고객 정보");
	Label name = new Label("이름 : ");
	TextField namet = new TextField();
	Label joinid = new Label("아이디 : ");
	TextField idt = new TextField();
	Label mail = new Label("E-mail : ");
	Label mailt = new Label();
	Label phone = new Label("전화번호 : ");
	Label phonet = new Label();
	Button src = new Button("검색");
	Button del = new Button("삭제하기");
	Button close  = new Button("닫기");
	
	Managedelet(){
		super("고객관리 프로그램");
		this.setSize(350, 450);
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
		this.add(Title); this.add(Title2); this.add(joinid); this.add(idt); this.add(src);
		this.add(name); this.add(namet); 
		this.add(mail); this.add(mailt); this.add(phone); this.add(phonet); this.add(del); this.add(close);
		
		Title.setFont(font15); Title2.setFont(font13); name.setFont(font13); mail.setFont(font13); phone.setFont(font13);
		
		Title.setBounds(130, 50, 200, 30); Title2.setBounds(110, 170, 200, 30);
		
		name.setBounds(30, 90, 50, 30); namet.setBounds(80, 90, 80, 30);
		joinid.setBounds(170, 90, 70, 30); idt.setBounds(240, 90, 80, 30);
		src.setBounds(50, 130, 250, 30);
		mail.setBounds(60, 240, 70, 30); mailt.setBounds(140, 240, 90, 30);
		phone.setBounds(50, 280, 70, 30); phonet.setBounds(140, 280, 90, 30);
		del.setBounds(130, 330, 80, 30);
		close.setBounds(140, 370, 60, 30);
		
	}
	void close() {this.setVisible(false); ManageMain main= new ManageMain();}
	void start() {
		src.addActionListener(this); del.addActionListener(this); close.addActionListener(this);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==src) {
			String name = namet.getText();
			String joinid = idt.getText();
			if(name.equals("")) {msgDlg("이름을 입력하세요"); return;}
			if(joinid.equals("")) {msgDlg("아이디을 입력하세요"); return;}
			
			Search(name, joinid);
		}
		else if(e.getSource()==del) {
			String name = namet.getText();
			String joinid = idt.getText();
			if(name.equals("")) {msgDlg("이름을 입력하세요"); return;}
			if(joinid.equals("")) {msgDlg("아이디를 입력하세요"); return;}
			delet(name, joinid);
			msgDlg("삭제가 완료되었습니다");
			namet.setText("");
			idt.setText("");
			mailt.setText("");
			phonet.setText("");
		}
		else if(e.getSource()==close) {close();}
		
	}
	
	void Search(String name, String joinid){
		boolean check=false;
		String query = "select * from tb_javaproject where name = ? and id = ?";
		
		try {
			PreparedStatement pstmt = dc.prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.setString(2, joinid);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				check=true;
				mailt.setText(rs.getString("email"));
				phonet.setText(rs.getString("phone"));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException ee) {
			System.err.println("login 처리 실패!!");
		}
		if(check==false) {msgDlg("존재하지 않는 아이디");}
	}
	
	void delet(String name, String joinid) {
		String query = "delete from tb_javaproject where name = ? and id = ?";
		
		try {
			PreparedStatement pstmt = dc.prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.setString(2, joinid);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException ee) {
			System.err.println("회원 삭제 실패!!");
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
	
}
