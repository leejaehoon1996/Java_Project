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
import java.sql.ResultSet;
import java.sql.SQLException;

public class Join extends Frame implements ActionListener, ItemListener{

	String url = "jdbc:mysql://localhost:3306/java_db?useUnicode=true&characterEncoding=utf8";
	String id = "root";
	String pass = "qwer";
	private Connection dc=null;
	
	Font font20 = new Font("TimesRoman", Font.BOLD, 17);
	Font font15 = new Font("TimesRoman", Font.BOLD, 15);
	Font font13 = new Font("TimesRoman", Font.BOLD, 13);
	Font font12b = new Font("TimesRoman", Font.BOLD, 12);
	
	boolean idcheck = false;
	
	Label Title = new Label("회원가입");
	Label name = new Label("이름 : ");
	TextField namet = new TextField();
	Label joinid = new Label("아이디 : ");
	TextField idt = new TextField();
	Label pw = new Label("비밀번호 : ");
	TextField pwt = new TextField();
	Label mail = new Label("E-mail : ");
	TextField mailt = new TextField();
	Label phone = new Label("전화번호 : ");
	TextField phonet = new TextField();
	Button check = new Button("중복체크");
	Button join = new Button("회원가입");
	Button close  = new Button("닫기");
	
	
	Join(){
		super("회원가입");
		this.setSize(360, 410);
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
		this.add(name); this.add(namet); this.add(joinid); this.add(idt); this.add(pw); this.add(pwt);
		this.add(mail); this.add(mailt); this.add(phone); this.add(phonet); this.add(join); this.add(check); this.add(close);
		
		Title.setFont(font15); name.setFont(font13); joinid.setFont(font13); pw.setFont(font13); mail.setFont(font13); phone.setFont(font13);
		
		Title.setBounds(140, 50, 200, 30);
		joinid.setBounds(65, 100, 60, 30); idt.setBounds(130, 100, 90, 30); check.setBounds(230, 100, 80, 30);
		name.setBounds(80, 140, 50, 30); namet.setBounds(130, 140, 90, 30);
		pw.setBounds(50, 180, 70, 30); pwt.setBounds(130, 180, 90, 30);
		mail.setBounds(60, 220, 70, 30); mailt.setBounds(130, 220, 90, 30);
		phone.setBounds(50, 260, 70, 30); phonet.setBounds(130, 260, 90, 30);
		join.setBounds(140, 300, 80, 30); 
		close.setBounds(150, 340, 60, 30);
	}
	
	void close() {this.setVisible(false);}
	
	void start() {
		join.addActionListener(this); close.addActionListener(this); check.addActionListener(this);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==check) {
			String joinid = idt.getText();
			joinidcheck(joinid);
		}
		else if(e.getSource()==join) {
			String name = namet.getText();
			String joinid = idt.getText();
			String pw = pwt.getText();
			String mail = mailt.getText();
			String phone = phonet.getText();
			if(joinid.equals("")) {msgDlg("아이디을 입력하세요"); return;}
			if(name.equals("")) {msgDlg("이름을 입력하세요"); return;}
			if(pw.equals("")) {msgDlg("비밀번호을 선택하세요"); return;}
			if(mail.equals("")) {msgDlg("메일을 선택하세요"); return;}
			if(phone.equals("")) {msgDlg("전화번호을 입력하세요"); return;}
			
			//DB처리
			Insert(name, joinid, pw, mail, phone);
			
			msgDlg("회원가입이 되었습니다");
			close();
		}
		
		else if(e.getSource()==close) {close();}
	}
	
	void Insert(String name, String joinid, String pw, String mail, String phone) {
		String query = "insert into tb_javaproject values(null, ?, ?, ?, ?, ?)";
		
		try {
			PreparedStatement pstmt = dc.prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.setString(2, joinid);
			pstmt.setString(3, pw);
			pstmt.setString(4, mail);
			pstmt.setString(5, phone);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException ee) {
			System.err.println("회원 가입 실패!! : " + ee.toString());
		}
	}
	void joinidcheck(String joinid) {
		boolean idCheck = false;//중복된 아이디가 있으면 true;
		String query = "select * from tb_javaproject where id = ?";
		try {
			PreparedStatement pstmt = dc.prepareStatement(query);
			pstmt.setString(1, joinid);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				msgDlg("아이디 중복");
				idCheck = true;
			}
			rs.close();
			pstmt.close();
			} catch (SQLException ee) {
			System.err.println("중복처리 처리 실패!!");
			}
		if(idCheck==false)
		{
			msgDlg("아이디 생성가능!");
			idt.setEnabled(false);
			idt.setEnabled(false);
			
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
