package com.puxinxiaolin.adopt.service;

import com.puxinxiaolin.adopt.entity.dto.PetDTO;
import com.puxinxiaolin.adopt.entity.dto.PetQueryDTO;
import com.puxinxiaolin.adopt.entity.entity.Pet;
import com.puxinxiaolin.adopt.entity.vo.PetVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 宠物服务接口
 */
public interface PetService extends IService<Pet> {

    /**
     * 分页查询宠物列表
     *
     * @param queryDTO 查询条件
     * @return 宠物分页数据
     */
    Page<PetVO> queryPetPage(PetQueryDTO queryDTO);

    /**
     * 根据ID查询宠物详情
     *
     * @param id 宠物ID
     * @return 宠物详情
     */
    PetVO getPetDetail(Long id);

    /**
     * 创建宠物
     *
     * @param petDTO 宠物DTO
     * @return 宠物ID
     */
    PetVO createPet(PetDTO petDTO);

    /**
     * 更新宠物
     *
     * @param id     宠物ID
     * @param petDTO 宠物DTO
     * @return 是否成功
     */
    boolean updatePet(Long id, PetDTO petDTO);

    /**
     * 删除宠物
     */
    void deletePet(Long id);

    /**
     * 批量删除宠物
     */
    void batchDeletePet(List<Long> ids);

    /**
     * 获取推荐宠物
     */
    List<PetVO> getRecommendedPets(Integer limit);

    /**
     * 增加宠物浏览次数
     *
     * @param id 宠物ID
     */
    void incrementViewCount(Long id);

    /**
     * 更新宠物上架状态
     *
     * @param id          宠物ID
     * @param shelfStatus 上架状态
     * @return 是否成功
     */
    boolean updateShelfStatus(Long id, Integer shelfStatus);

    /**
     * 更新宠物领养状态
     *
     * @param id             宠物ID
     * @param adoptionStatus 领养状态
     * @return 是否成功
     */
    boolean updateAdoptionStatus(Long id, String adoptionStatus);

    /**
     * 更新宠物领养状态和领养者
     *
     * @param id             宠物ID
     * @param adoptionStatus 领养状态
     * @param adoptedBy      领养者ID
     * @return 是否成功
     */
    boolean updateAdoptionStatusAndAdoptedBy(Long id, String adoptionStatus, Long adoptedBy);

    /**
     * 上传宠物图片
     */
    String uploadPetImage(Long id, MultipartFile file);

    /**
     * 上传宠物封面图片
     */
    String uploadPetCover(Long id, MultipartFile file);

    /**
     * 获取随机宠物图片
     */
    List<String> getRandomPetImages(Integer limit);
}


