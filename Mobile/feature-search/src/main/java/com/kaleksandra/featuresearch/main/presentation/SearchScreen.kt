package com.kaleksandra.featuresearch.main.presentation

import android.widget.TextView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kaleksandra.coredata.network.model.Area
import com.kaleksandra.coredata.network.model.Item
import com.kaleksandra.corenavigation.ProfileDirection
import com.kaleksandra.coretheme.Dimen
import com.kaleksandra.featuresearch.main.domain.model.SearchVacancyRequest
import com.kaleksandra.featuresearch.main.domain.model.Skill
import com.kaleksandra.featuresearch.main.domain.model.VacancyItemPresentation

@Composable
fun SearchScreen(
    navController: NavController, viewModel: SearchViewModel = hiltViewModel()
) {
    val cities by viewModel.cities.collectAsState()
    val vacancies by viewModel.vacancies.collectAsState()
    val findedSkills by viewModel.findedSkills.collectAsState()
    SearchScreen(
        cities,
        vacancies,
        findedSkills,
        viewModel::searchCity,
        viewModel::searchVacancy,
        {
            navController.navigate(ProfileDirection.path)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    cities: List<Area>,
    vacancies: List<VacancyItemPresentation>,
    skills: List<Skill>,
    onCitySearch: (String) -> Unit = {},
    onSearchVacancy: (SearchVacancyRequest) -> Unit,
    onProfileClick: () -> Unit,
) {
    var searchText by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf<Area?>(null) }
    val experience = listOf(
        Item("noExperience", "Нет опыта"),
        Item("between1And3", "От 1 года до 3 лет"),
        Item("between3And6", "От 3 до 6 лет"),
        Item("moreThan6", "Более 6 лет"),
    )
    var selectedExperience by remember { mutableStateOf<Item?>(null) }
    val employment = listOf(
        Item("full", "Полная занятость"),
        Item("part", "Частичная занятость"),
        Item("project", "Проектная работа"),
        Item("volunteer", "Волонтёрство"),
        Item("probation", "Стажировка"),
    )
    var selectedEmployment by remember { mutableStateOf<Item?>(null) }
    val schedule = listOf(
        Item("fullDay", "Полный день"),
        Item("shift", "Сменный график"),
        Item("flexible", "Гибкий график"),
        Item("remote", "Удалённая работа"),
        Item("flyInFlyOut", "Вахтовый метод"),
    )
    var selectedSchedule by remember { mutableStateOf<Item?>(null) }
    var checkedSalary by remember { mutableStateOf(false) }
    var isOpen by remember { mutableStateOf(true) }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimen.padding_16)
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = onProfileClick,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Outlined.AccountCircle,
                        contentDescription = "Профиль",
                    )
                }
            }
        }
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isOpen = !isOpen }
                        .padding(Dimen.padding_12),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Параметры поиска",
                        modifier = Modifier.weight(1f)

                    )
                    Icon(
                        if (isOpen) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                        contentDescription = "Открыть параметры поиска"
                    )
                }
                AnimatedVisibility(visible = isOpen) {
                    Column() {
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                            },
                            label = { Text("Профессия, должность") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = city,
                            onValueChange = {
                                city = it
                                onCitySearch(it)
                            },
                            label = { Text("Город") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(cities) { city ->
                                FilterChip(
                                    selected = selectedCity == city,
                                    onClick = {
                                        selectedCity = city
                                    },
                                    label = {
                                        Text(text = city.name)
                                    },
                                )
                            }
                        }
                        DropBox(
                            title = "Опыт работы",
                            itemList = experience,
                            selected = selectedExperience,
                            onSelected = { selectedExperience = it },
                            modifier = Modifier.padding(top = Dimen.padding_16)
                        )
                        DropBox(
                            title = "Тип занятости",
                            itemList = employment,
                            selected = selectedEmployment,
                            onSelected = { selectedEmployment = it },
                            modifier = Modifier.padding(top = Dimen.padding_16)
                        )
                        DropBox(
                            title = "График работы",
                            itemList = schedule,
                            selected = selectedSchedule,
                            onSelected = { selectedSchedule = it },
                            modifier = Modifier.padding(top = Dimen.padding_16)
                        )
                        Text(
                            "Только с зарплатой",
                            modifier = Modifier.padding(top = Dimen.padding_16)
                        )
                        Switch(checked = checkedSalary,
                            onCheckedChange = { checkedSalary = it })
                    }
                }
            }
        }
        item {
            Button(
                {
                    onSearchVacancy(
                        SearchVacancyRequest(
                            area = selectedCity?.id,
                            text = searchText,
                            experience = selectedExperience?.id,
                            employment = selectedEmployment?.id,
                            schedule = selectedSchedule?.id,
                            onlyWithSalary = checkedSalary
                        )
                    )
                    isOpen = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimen.padding_16)
            ) {
                Text("Найти вакансии")
            }
        }
        item {
            if (vacancies.isNotEmpty()) {
                Text(
                    "Самые популярные скиллы",
                    modifier = Modifier.padding(
                        top = Dimen.padding_16,
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimen.padding_8, bottom = Dimen.padding_8),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(skills) { skill ->
                    SuggestionChip(
                        {},
                        { Text(skill.name) },
                        modifier = Modifier.height(32.dp),
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor =
                            if (skill.isSelected) Color.Green else Color.Transparent
                        )
                    )
                }
            }
        }
        items(vacancies) { vacancy ->
            var isOpenVacancy by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimen.padding_4)
                    .clickable { isOpenVacancy = !isOpenVacancy }
                    .padding(Dimen.padding_12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = vacancy.name,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    if (isOpen) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                    contentDescription = "Открыть параметры поиска"
                )
            }
            Text(
                "Количество совпадений: ${vacancy.procentage}%",
                modifier = Modifier.padding(top = Dimen.padding_4, bottom = Dimen.padding_8),
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
            AnimatedVisibility(visible = isOpenVacancy) {
                AndroidView(
                    modifier = Modifier.padding(top = Dimen.padding_16),
                    factory = { context -> TextView(context) },
                    update = {
                        it.text =
                            HtmlCompat.fromHtml(
                                vacancy.description,
                                HtmlCompat.FROM_HTML_MODE_COMPACT
                            )
                    }
                )
            }
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(vacancy.skills) { skill ->
                    SuggestionChip(
                        {},
                        { Text(skill.name) },
                        modifier = Modifier.height(32.dp),
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor =
                            if (skill.isSelected) Color.Green else Color.Transparent
                        )
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropBox(
    title: String,
    itemList: List<Item>,
    selected: Item?,
    onSelected: (Item) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    Box(modifier.fillMaxWidth()) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = selected?.name ?: "",
                onValueChange = {},
                readOnly = true,
                placeholder = { Text(title) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = isExpanded
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                itemList.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(item.name)
                        },
                        onClick = {
                            onSelected(item)
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillsChip(
    value: String, isSelected: (Boolean, String) -> Unit, modifier: Modifier = Modifier
) {
    var selected by remember { mutableStateOf(false) }
    FilterChip(selected = selected, onClick = {
        selected = !selected
        isSelected(selected, value)
    }, label = {
        Text(text = value)
    }, modifier = modifier
    )
}
