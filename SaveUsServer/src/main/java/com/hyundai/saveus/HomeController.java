package com.hyundai.saveus;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

/**
 * Handles requests for the application home page.
 */

@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/insert.do", method = RequestMethod.POST)
	public String insert(HttpServletRequest request2,MultipartHttpServletRequest request, ModelMap model)
			throws IllegalStateException, IOException {
		System.out.println("들어옴");
		Map<String, MultipartFile> files = request.getFileMap();
		CommonsMultipartFile cmf = (CommonsMultipartFile) files.get("photo");
		// 경로
		String pdfPath = request.getSession().getServletContext().getRealPath("/resources");
		System.out.println("pdf : " + pdfPath);
		String savePath = ""+pdfPath+"/common/img/"+cmf.getOriginalFilename();
		System.out.println("저장 경로 : " +savePath);

		File file = new File(savePath);
		// 파일 업로드 처리 완료.
		cmf.transferTo(file);

		try {
			// insert method
			model.addAttribute("result", "업로드 성공");
		} catch (Exception e) {
			model.addAttribute("result", "업로드 실패");
		}
		sendPush();
		return "fileupload";
	}

	@RequestMapping(value = "/home.do", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "Hello";
	}

	@RequestMapping(value = "/receiver.do")
	public String test(HttpServletRequest request, Model model) throws Exception {
		String id = request.getParameter("id");
		String passwd = request.getParameter("pw");
		System.out.println("id : " + id + " pwd : " + passwd);
		model.addAttribute("id", id);
		return "home";
	}

	@RequestMapping(value = "/push.do")
	public void msgSend(HttpServletRequest request) throws Exception {
		sendPush();
	}
	
	public void sendPush() {
		String msg = "움직임 포착!!";
		String register_id="APA91bGGbLhsjufEDdz90wulEfrb6AQGvanOqHzzRyp8q8gbgRt0-N2Ju48RGorQv3H5GB-WcAeUKd4-tuujRsp7LgANzoM59etxz6tJv7Lj0WqUmbDDU_7r5MEy3tP1pIzTTg1UQZ9s";
		try {
			
			msg = URLEncoder.encode(msg, "euc-kr");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String simpleApiKey = "AIzaSyBcjjjjShUZgjaLk2aq2Cbc4oqE_nWMiFw";
		ArrayList<String> regid = new ArrayList<String>(); // reg_id

		String MESSAGE_ID = String.valueOf(Math.random() % 100 + 1);

		boolean SHOW_ON_IDLE = false;

		int LIVE_TIME = 1 ; 

		int RETRY = 2;
		regid.add(register_id);
		Sender sender = new Sender(simpleApiKey);
		Message message = new Message.Builder()

				.collapseKey(MESSAGE_ID)

				.delayWhileIdle(SHOW_ON_IDLE)

				.timeToLive(LIVE_TIME).addData("msg", msg)

				.build();

		MulticastResult result1 = null;
		try {
			result1 = sender.send(message, regid, RETRY);
			System.out.println(result1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (result1 != null) {

			List<Result> resultList = result1.getResults();

			for (Result result : resultList) {

				System.out.println(result.getErrorCodeName());

			}

		}

	}

}
