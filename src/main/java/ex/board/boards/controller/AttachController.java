package ex.board.boards.controller;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import ex.board.boards.domain.Attach;
import ex.board.boards.domain.repository.AttachRepository;
import ex.board.boards.service.dto.AttachDto;
import ex.board.boards.service.dto.AttachListDto;

@RestController
public class AttachController {

    private final AttachRepository attachRepository;

    public AttachController(AttachRepository attachRepository) {
        this.attachRepository = attachRepository;
    }

    Logger log = LoggerFactory.getLogger(AttachController.class);

    @Value("${upload.path}\\")
    private String uploadPath;
    private String makeFoler() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        log.info("str : " + str);
        String folderpath = str.replace("/", File.separator);
        log.info("folderpath : " + folderpath);

        // make folder
        File uploadPathFoler = new File(uploadPath, folderpath);
        // File newFile = new File(dir, "파일명");
        // -> 부모 디렉토리를 파라미터로 인스턴스 생성

        if(uploadPathFoler.exists() == false) {
            uploadPathFoler.mkdirs();
            // 만약 uploadPathFolder가 존재하지 않는다면 makeDirectory하라는 의미
            // mkdir(): 디렉토리에 상위 디렉토리가 존재하지 않을경우에는 생성이 불가능한 함수
            // mkdirs(): 디렉토리의 상위 디렉토리가 존재하지 않을 경우에는 상위 디렉토리까지 모두 생성하는 함수
        }

        return folderpath;
    }

    @PostMapping(value="/multiImageUploader")
    public void smarteditorMultiImageUpload(HttpServletRequest request, HttpServletResponse response){
        try {
            //파일정보
            String sFileInfo = "";
            //파일명을 받는다 - 일반 원본파일명
            String sFilename = request.getHeader("file-name");
            sFilename = URLDecoder.decode(sFilename, "UTF-8");
            //파일 확장자
            String sFilenameExt = sFilename.substring(sFilename.lastIndexOf(".")+1);
            //확장자를소문자로 변경
            sFilenameExt = sFilenameExt.toLowerCase();

            //이미지 검증 배열변수
            String[] allowFileArr = {"jpg","png","bmp","gif"};

            //확장자 체크
            int nCnt = 0;
            for(int i=0; i<allowFileArr.length; i++) {
                if(sFilenameExt.equals(allowFileArr[i])){
                    nCnt++;
                }
            }

            //이미지가 아니라면
            if(nCnt == 0) {
                PrintWriter print = response.getWriter();
                print.print("NOTALLOW_"+sFilename);
                print.flush();
                print.close();
            } else {
                //디렉토리 설정 및 업로드

                //파일경로
                String folderPath = makeFoler();
                String filePath = uploadPath + File.separator + folderPath;
                log.info("filePath : " + filePath);
                File file = new File(filePath);

                String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));

                String uuid = UUID.randomUUID().toString();

                String sRealFileNm = "";
                sRealFileNm = str + uuid + "_" + sFilename;
                String rlFileNm = filePath + File.separator +sRealFileNm;
                log.info("rlFileNm : " + rlFileNm);

                ///////////////// 서버에 파일쓰기 /////////////////
                InputStream inputStream = request.getInputStream();
                OutputStream outputStream = new FileOutputStream(rlFileNm);
                int numRead;
                byte bytes[] = new byte[Integer.parseInt(request.getHeader("file-size"))];
                while((numRead = inputStream.read(bytes,0,bytes.length)) != -1){
                    outputStream.write(bytes,0,numRead);
                }
                if(inputStream != null) {
                    inputStream.close();
                }
                outputStream.flush();
                outputStream.close();

                ///////////////// 이미지 /////////////////
                // 정보 출력
                sFileInfo += "&bNewLine=true";
                // img 태그의 title 속성을 원본파일명으로 적용시켜주기 위함
                sFileInfo += "&sFileName="+ sFilename;
//                String path = rlFileNm.replace(File.separator, "|~|");
//                path = URLEncoder.encode(path, "UTF-8");
                String url = URLEncoder.encode(sRealFileNm, "UTF-8");
                sFileInfo += "&sFileURL=/" + url;
                log.info(sRealFileNm);
                PrintWriter printWriter = response.getWriter();
                printWriter.print(sFileInfo);
                printWriter.flush();
                printWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/{sRealFileNm}")
    public ResponseEntity<byte[]> getFiles(@PathVariable("sRealFileNm") String fileName) {
        ResponseEntity<byte[]> result = null;

        try{
            String srcFileName = URLDecoder.decode(fileName,"UTF-8");
            String folderPath = fileName.substring(0,10).replace("_",File.separator);
            String path = uploadPath + File.separator + folderPath;
            log.info("filename : " + srcFileName);
            File file = new File(path + File.separator + srcFileName);

//            File file = new File(path);
            log.info("file : " + file);
            HttpHeaders header = new HttpHeaders();

            // MIME 타입 처리
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            // File 객체를 Path로 변환하여 MIME 타입을 판단하여 HTTPHeaders의 Content-Type에 값으로 들어갑니다.

            // 파일 데이터 처리 *FileCopyUtils.copy 아래에 정리
            // new ResponseEntity(body, header, status)
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  result;
    }

    @PostMapping("/uploadFiles")
    public ResponseEntity<List<AttachDto>> upload(@RequestParam MultipartFile[] uploadFiles) {

        List<AttachDto> resultUploadList = new ArrayList<>();

        for (MultipartFile file : uploadFiles) {

            String originalName = file.getOriginalFilename();
            log.info("originalName : ", originalName);
            originalName = originalName.substring(originalName.lastIndexOf("//") + 1);

            log.info("originalName : " + originalName);

            String folderPath = makeFoler();
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + "_" + originalName;
            Path savaPath = Paths.get(uploadPath + File.separator + folderPath + File.separator + fileName);

            try {
                // 원본 파일 저장
                file.transferTo(savaPath);
                log.info("saveName = {}", uploadPath + File.separator + folderPath + File.separator + fileName);
                AttachDto attachDTO = new AttachDto(fileName, originalName, folderPath);
                resultUploadList.add(attachDTO);
            } catch (IOException e) {
                log.info("ex", e);
                throw new RuntimeException(e);
            }
        } // end for
        return new ResponseEntity<>(resultUploadList, HttpStatus.OK);
    }

    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName) {

        String srcFileName = null;

        try {
            srcFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("delete:" + srcFileName);
            // UUID가 포함된 파일이름을 디코딩해줍니다.
            File file = new File(uploadPath + File.separator + srcFileName);
            boolean result = file.delete();
            return  new ResponseEntity<>(result, HttpStatus.OK);
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value= "/attachList/{boardId}")
    public ResponseEntity<List<AttachListDto>> getUploadFilesList(@PathVariable(name = "boardId") Long boardId)
            throws Exception {
        List<Attach> attachList = new ArrayList<>(attachRepository.findAttachList(boardId));
        List<AttachListDto> attachListDtos = new ArrayList<>();
        for (Attach attach : attachList) {
            AttachListDto attachListDto = new AttachListDto(attach.getId(), attach.getOriginalName());
            attachListDtos.add(attachListDto);
        }

        return new ResponseEntity<>(attachListDtos, HttpStatus.OK);
    }

    @GetMapping("/download/{attachId}")
    public ResponseEntity<Resource> download(@PathVariable Long attachId) throws Exception {
        Optional<Attach> findAttach = attachRepository.findById(attachId);
        String fileName = findAttach.map(Attach::getFileName)
                            .orElse("파일이름없음");
        String folderPath = findAttach.map(Attach::getFolderPath)
                            .orElse("폴더경로없음");
        String originalName = findAttach.map(Attach::getOriginalName)
                .orElse("파일오리지널이름없음");
        log.info("fileName : {}", fileName);

        AttachDto attachDto = new AttachDto(fileName, originalName, folderPath);
        UrlResource resource = new UrlResource("file:" + uploadPath + File.separator +
                attachDto.getFolderPath() + File.separator + attachDto.getFileName());
        String encodedUploadFileName = UriUtils.encode(attachDto.getOriginalName(), StandardCharsets.UTF_8);

        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
