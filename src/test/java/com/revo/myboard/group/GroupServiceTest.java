package com.revo.myboard.group;

import com.revo.myboard.group.dto.CreateDTO;
import com.revo.myboard.user.UserServiceApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    private static final String TEST_NAME = "UŻYTKOWNIK";
    private static final String TEST_NAME_NEW = "NEW NAME";

    @Mock
    private GroupRepository groupRepository;
    @Mock
    private UserServiceApi userServiceApi;

    @InjectMocks
    private GroupService groupService;

    private Group testGroup;
    private CreateDTO testCreateDTO;

    @BeforeEach
    void init(){
        testGroup = Group.builder()
                .id(1L)
                .authority(Authority.USER)
                .name(TEST_NAME)
                .build();
        testCreateDTO = new CreateDTO();
        testCreateDTO.setName(TEST_NAME);
        testCreateDTO.setAuthority(Authority.USER.toString());
    }

    @Test
    void getGroupDTOById() {
        //given
        Mockito.when(groupRepository.findById(1L)).thenReturn(Optional.of(testGroup));
        //then
        var groupDTO = groupService.getGroupDTOById(1L);
        //then
        Assertions.assertEquals(groupDTO.getName(), TEST_NAME);
        Assertions.assertEquals(groupDTO.getId(), 1L);
        Assertions.assertEquals(groupDTO.getAuthority(), Authority.USER.toString());
    }

    @Test
    void createGroup() {
        //given
        Mockito.when(groupRepository.existsByName(TEST_NAME)).thenReturn(false);
        Mockito.when(groupRepository.save(Mockito.any(Group.class))).thenReturn(testGroup);
        //then
        var groupDTO = groupService.createGroup(testCreateDTO);
        //then
        Assertions.assertEquals(groupDTO.getName(), TEST_NAME);
        Assertions.assertEquals(groupDTO.getId(), 1L);
        Assertions.assertEquals(groupDTO.getAuthority(), Authority.USER.toString());
    }

    @Test
    void renameGroupById() {
        //given
        Mockito.when(groupRepository.existsByName(TEST_NAME_NEW)).thenReturn(false);
        Mockito.when(groupRepository.findById(1L)).thenReturn(Optional.of(testGroup));
        //when
        var groupDTO = groupService.renameGroupById(1, TEST_NAME_NEW);
        //then
        Assertions.assertEquals(groupDTO.getName(), TEST_NAME_NEW);
        Assertions.assertEquals(groupDTO.getId(), 1L);
        Assertions.assertEquals(groupDTO.getAuthority(), Authority.USER.toString());
    }

    @Test
    void getAllGroups() {
        //given
        Mockito.when(groupRepository.findAll()).thenReturn(Arrays.asList(testGroup));
        //when
        var listOfGroupDTO = groupService.getAllGroups();
        //then
        Assertions.assertEquals(listOfGroupDTO.size(), 1);
        Assertions.assertEquals(listOfGroupDTO.get(0).getName(), TEST_NAME);
        Assertions.assertEquals(listOfGroupDTO.get(0).getId(), 1L);
        Assertions.assertEquals(listOfGroupDTO.get(0).getAuthority(), Authority.USER.toString());
    }

    @Test
    void changeGroupAuthority() {
        //given
        Mockito.when(groupRepository.findById(1L)).thenReturn(Optional.of(testGroup));
        //when
        var groupDTO = groupService.changeGroupAuthority(1L, Authority.MODERATOR.toString());
        //then
        Assertions.assertEquals(groupDTO.getName(), TEST_NAME);
        Assertions.assertEquals(groupDTO.getId(), 1L);
        Assertions.assertEquals(groupDTO.getAuthority(), Authority.MODERATOR.toString());
    }
}