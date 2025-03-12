package com.digiquad.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.digiquad.model.RowData;
import com.digiquad.service.FileService;

@Controller
public class FileController {
	private final FileService fileService;

	public FileController(FileService fileProcessingService) {
		this.fileService = fileProcessingService;
	}

	@GetMapping("/")
	public String uploadPage() {
		return "upload";
	}

	@PostMapping("/upload")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, 
			@RequestParam("startRow") int startRow,
			@RequestParam ("endRow") int endRow,
			Model model) {
		try {
			List<RowData> rowDataList = fileService.processFile(file, startRow, endRow);
			model.addAttribute("rows", rowDataList);
			return "display";
		} catch (IOException e) {
			model.addAttribute("error", "Error processing = " + e.getMessage());
			return "upload";
		}
	}

	@GetMapping("/home")
	public String home() {
		return "home";
	}
	@GetMapping("/getData")
	public String getForm()
	{
		return "form_page";
	}
	
	@PostMapping("/showData")
	public String showRoWColumnData(@RequestParam("file") MultipartFile file,@RequestParam("row") Integer row,@RequestParam("col") Integer col,Model model) throws Exception
	{
		RowData data =fileService.getOnlyValue(file, row, col);
		model.addAttribute("value",data.getValue());
		return "show_value";
	}
}
