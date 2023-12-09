package com.example.rapidauditpro

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rapidauditpro.ui.theme.RapidAuditProTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RapidAuditProTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "start") {
                    composable("start") {
                        StartScreen (
                            onNavigateToForm = { navController.navigate("form") },
                            onNavigateToProductList = { navController.navigate("productList") }
                        )
                    }
                    composable("form") {
                        RapidLayout (
                            onNavigateBack = { navController.popBackStack("start", inclusive = false) },
                            onEnviarClick = { product ->
                                println("Nuevo producto: $product")
                            }
                        )
                    }
                    composable("productList") {
                        val products =  listOf(
                            Product("Prueba Componente 1", "Equipo A", "Campus 1", "Departmento X"),
                            Product("Prueba Componente 2", "Equipo B", "Campus 2", "Departmento Y"),
                            Product("Prueba Componente 3", "Equipo C", "Campus 3", "Departmento Z")
                        )
                        ProductListScreen(
                            products = products,
                            onEditClick = { },
                            onDeleteClick = { },
                            onNavigateBack = { navController.navigate("start") }
                        )
                    }
                }

            }
        }
    }
}
@Composable
fun RapidLayout(onNavigateBack: () -> Unit, onEnviarClick: (Product) -> Unit) {
    var productNameInput by remember { mutableStateOf("ZSG400") }
    var controlTeamInput by remember { mutableStateOf("TM4-TVZ") }
    var localizationInput by remember { mutableStateOf("Monterrey-01") }
    var departmentInput by remember { mutableStateOf("Automotriz") }
    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.CenterHorizontally),
            fontSize = 30.sp
        )
        Text(
            text = stringResource(R.string.audit),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.CenterHorizontally),
            fontSize = 25.sp
        )
        Text(
            text = stringResource(R.string.location),
            fontSize = 20.sp
        )
        EditLocalizationField(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            localizationInput = localizationInput,
            onValueChange = { localizationInput = it }
        )
        Text(
            text = stringResource(R.string.department),
            fontSize = 20.sp
        )
        EditDepartmentField(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            departmentInput = departmentInput,
            onValueChange = { departmentInput = it }
        )
        Text(
            text = stringResource(R.string.controlTeam),
            fontSize = 20.sp
        )
        EditControlTeamField(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            controlTeamInput = controlTeamInput,
            onValueChange = { controlTeamInput = it }
        )
        Text(
            text = stringResource(R.string.productName),
            fontSize = 20.sp
        )
        EditProductNameField(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            productNameInput = productNameInput,
            onValueChange = { productNameInput = it }
        )
        Button(onClick = {
            val product = Product(
                productName = productNameInput,
                controlTeam = controlTeamInput,
                localization = localizationInput,
                department = departmentInput
            )
            onEnviarClick(product);
            showDialog = true }) {
            Text("Enviar")
        }

        if (showDialog) {
            ShowValuesDialog(productNameInput, controlTeamInput, localizationInput, departmentInput, onDismissRequest = { showDialog = false })
        }

        Button(onClick = { onNavigateBack() }) {
            Text("Regresar")
        }

        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun ShowValuesDialog(
    productName: String,
    controlTeam: String,
    localization: String,
    department: String,
    onDismissRequest: () -> Unit
) {
    val dialogTitle = "Componente registrado"
    val dialogMessage = buildString {
        appendLine("Nombre del componente: $productName")
        appendLine("Equipo de control: $controlTeam")
        appendLine("Localización: $localization")
        appendLine("Departamento: $department")
    }

    AlertDialog(
        onDismissRequest = { /* Handle dialog dismiss if needed */ },
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogMessage) },
        confirmButton = {
            Button(
                onClick = { onDismissRequest() }
            ) {
                Text("OK")
            }
        }
    )
}

@Composable
fun EditProductNameField(
    modifier: Modifier = Modifier,
    productNameInput: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = productNameInput,
        onValueChange = onValueChange,
        modifier = modifier
    )
}

@Composable
fun EditControlTeamField(
    modifier: Modifier = Modifier,
    controlTeamInput: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = controlTeamInput,
        onValueChange = onValueChange,
        modifier = modifier
    )
}

@Composable
fun EditLocalizationField(
    modifier: Modifier = Modifier,
    localizationInput: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = localizationInput,
        onValueChange = onValueChange,
        modifier = modifier
    )
}

@Composable
fun EditDepartmentField(
    modifier: Modifier = Modifier,
    departmentInput: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = departmentInput,
        onValueChange = onValueChange,
        modifier = modifier
    )
}

@Composable
fun StartScreen(onNavigateToForm: () -> Unit, onNavigateToProductList: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "RapidAuditPro",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Tu asistente de producción",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(
            onClick = { onNavigateToForm() }
        ) {
            Text("Registrar componente")
        }
        Button(
            onClick = {
                onNavigateToProductList()
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Mostrar lista de componentes")
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(products: List<Product>, onEditClick: (Product) -> Unit, onDeleteClick: (Product) -> Unit, onNavigateBack: () -> Unit) {
    var productList by remember { mutableStateOf(products) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de componentes (prototipo)") },
            )
        },
        content = {
            ProductList(products = products, onEditClick = onEditClick, onDeleteClick = {  product ->
                onDeleteClick(product)
                productList = productList.toMutableList().apply { remove(product) }
            })
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { onNavigateBack() }) {
            Text("Regresar")
        }
    }
}

@Composable
fun ProductList(products: List<Product>, onEditClick: (Product) -> Unit, onDeleteClick: (Product) -> Unit) {
    LazyColumn {
        items(products) { product ->
            ProductListItem(product = product, onEditClick = onEditClick, onDeleteClick = onDeleteClick)
            Divider()
        }
    }
}

@Composable
fun ProductListItem(product: Product, onEditClick: (Product) -> Unit, onDeleteClick: (Product) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Componente: ${product.productName}")
        Row {
            IconButton(onClick = { onEditClick(product) }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { onDeleteClick(product) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RapidAuditProTheme {

    }
}