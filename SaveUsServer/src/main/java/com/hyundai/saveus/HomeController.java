package com.hyundai.saveus;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

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

/**
 * Handles requests for the application home page.
 */

@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/insert.do", method = RequestMethod.POST)
	public String insert(MultipartHttpServletRequest request, ModelMap model)
			throws IllegalStateException, IOException {
		System.out.println("들어옴");
		Map<String, MultipartFile> files = request.getFileMap();
		CommonsMultipartFile cmf = (CommonsMultipartFile) files.get("photo");
		// 경로
		String s = System.getProperty("user.dir");
		System.out.println("경로 : " + s);
		String savePath = "C:\\"+cmf.getOriginalFilename();
		

		File file = new File(savePath);
		// 파일 업로드 처리 완료.
		cmf.transferTo(file);

		try {
			// insert method
			model.addAttribute("result", "업로드 성공");
		} catch (Exception e) {
			model.addAttribute("result", "업로드 실패");
		}
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

	@RequestMapping(value = "/json.do")
	public void selectUser(HttpServletRequest request) throws Exception {
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
		MultipartFile multipartFile = null;
		while (iterator.hasNext()) {
			multipartFile = multipartHttpServletRequest.getFile(iterator.next());
			if (multipartFile.isEmpty() == false) {
				System.out.println("------------- file start -------------");
				System.out.println("name : " + multipartFile.getName());

				System.out.println("filename : " + multipartFile.getOriginalFilename());
				System.out.println("size : " + multipartFile.getSize());
				System.out.println("-------------- file end --------------\n");
			}
		}
	}
	
	public void letterPush(String letter_id, String to_id, String to_name, String from_id, String from_name,
			String address, String latitude, String longitude, String content, String date, String register_id) {
		String msg = "새로운 편지가 도착했습니다!";
		try {
			to_name = URLEncoder.encode(to_name, "euc-kr");
			from_name = URLEncoder.encode(from_name, "euc-kr");
			address = URLEncoder.encode(address, "euc-kr");
			content = URLEncoder.encode(content, "euc-kr");
			msg = URLEncoder.encode(msg, "euc-kr");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String simpleApiKey = "AIzaSyDQ8J1e2CqzeGQ0y9sSMmlWyjM0ugk74P0";
		ArrayList<String> regid = new ArrayList<String>(); // reg_id

		String MESSAGE_ID = String.valueOf(Math.random() % 100 + 1);

		boolean SHOW_ON_IDLE = false;

		int LIVE_TIME = 1; 

		int RETRY = 2;
		regid.add(register_id);
		Sender sender = new Sender(simpleApiKey);
		Message message = new Message.Builder()

				.collapseKey(MESSAGE_ID)

				.delayWhileIdle(SHOW_ON_IDLE)

				.timeToLive(LIVE_TIME).addData("letter_id", letter_id).addData("msg", msg)
				.addData("to_id", to_id).addData("to_name", to_name).addData("from_id", from_id)
				.addData("from_name", from_name).addData("address", address).addData("latitude", latitude)
				.addData("longitude", longitude).addData("content", content).addData("date", date)

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
