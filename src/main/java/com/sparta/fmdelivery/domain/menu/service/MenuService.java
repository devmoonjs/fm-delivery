package com.sparta.fmdelivery.domain.menu.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.menu.dto.MenuRequest;
import com.sparta.fmdelivery.domain.menu.dto.MenuResponse;
import com.sparta.fmdelivery.domain.menu.entity.Menu;
import com.sparta.fmdelivery.domain.menu.repository.MenuRepository;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import com.sparta.fmdelivery.domain.shop.repository.ShopRepository;
import com.sparta.fmdelivery.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ShopRepository shopRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private String MENU_IMG_DIR = "menu/";

    /**
     * 메뉴 생성
     * @param authUser
     * @param request
     * @param multipartFile
     * @return MenuResponse
     */
    @Transactional
    public MenuResponse createMenu(AuthUser authUser, MenuRequest request, MultipartFile multipartFile) {
        Shop shop = getValidatedShop(request.getShopId(), authUser);

        // 파일 업로드 처리
        String imageUrl = uploadImageToS3(multipartFile);

        // Menu 엔티티 생성
        Menu menu = new Menu(request, shop, imageUrl); // 메뉴 엔티티에 이미지 URL 추가
        return MenuResponse.fromEntity(menuRepository.save(menu));
    }

    /**
     * 이미지 업로드 (임시파일 생성하지 않음)
     * @param multipartFile
     * @return
     */
    private String uploadImageToS3(MultipartFile multipartFile) {
        try {
            String fileName = MENU_IMG_DIR + UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();

            // 메타데이터 설정 (파일 크기와 콘텐츠 타입)
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            // S3에 파일 업로드
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata));

            // 업로드된 파일의 URL 반환
            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            throw new ApiException(ErrorStatus._FILE_UPLOAD_ERROR);
        }
    }

    /**
     * 가게 메뉴 조회
     * @param shopId
     * @return List<MenuResponse>
     */
    @Transactional(readOnly = true)
    public List<MenuResponse> getMenus(Long shopId) {
        return menuRepository.findAllByShopId(shopId).stream()
                .map(MenuResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 메뉴 업데이트
     * @param authUser
     * @param menuId
     * @param request
     * @param image
     * @return MenuResponse
     */
    @Transactional
    public MenuResponse updateMenu(AuthUser authUser, Long menuId, MenuRequest request, MultipartFile image) {
        Shop shop = getValidatedShop(request.getShopId(), authUser);
        Menu menu = getValidatedMenu(menuId);

        // 이미지가 있는 경우 새로운 이미지 업로드, 없으면 기존 이미지 유지
        String imageUrl = menu.getImageUrl(); // 기존 이미지 URL
        if (image != null && !image.isEmpty()) {
            imageUrl = uploadImageToS3(image); // 새 이미지 업로드
        }

        menu.updateMenu(request.getName(), request.getPrice(), request.getStatus(), imageUrl);
        return MenuResponse.fromEntity(menu);
    }


    /**
     * 메뉴 삭제
     * @param authUser
     * @param menuId
     * @param shopId
     */
    @Transactional
    public void deleteMenu(AuthUser authUser, Long menuId, Long shopId) {
        Shop shop = getValidatedShop(shopId, authUser);
        Menu menu = getValidatedMenu(menuId);

        menuRepository.delete(menu);
    }

    /**
     * 가게 유효성 검증 및 사장님 권한 확인
     * @param shopId
     * @param authUser
     * @return Shop
     */
    private Shop getValidatedShop(Long shopId, AuthUser authUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_SHOP));
        validateOwner(shop, authUser);
        return shop;
    }

    /**
     * 메뉴 유효성 검증
     * @param menuId
     * @return Menu
     */
    private Menu getValidatedMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_MENU));
    }

    /**
     * 사장님 권한 검증
     * @param shop
     * @param authUser
     */
    private void validateOwner(Shop shop, AuthUser authUser) {
        if (!shop.getUser().getId().equals(authUser.getId())) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_UPDATE_SHOP);
        }
    }
}
