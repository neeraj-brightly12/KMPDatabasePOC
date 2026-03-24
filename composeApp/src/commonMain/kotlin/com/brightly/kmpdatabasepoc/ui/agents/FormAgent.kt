package com.brightly.kmpdatabasepoc.ui.agents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * Form Agent - Intelligent form builder that handles validation and state management
 */
class FormAgent {
    private val fields = mutableStateMapOf<String, FormField>()

    data class FormField(
        val value: MutableState<String>,
        val label: String,
        val keyboardType: KeyboardType = KeyboardType.Text,
        val validator: (String) -> String? = { null },
        val errorMessage: MutableState<String?> = mutableStateOf(null)
    )

    fun addTextField(
        key: String,
        label: String,
        initialValue: String = "",
        keyboardType: KeyboardType = KeyboardType.Text,
        validator: (String) -> String? = { null }
    ) {
        fields[key] = FormField(
            value = mutableStateOf(initialValue),
            label = label,
            keyboardType = keyboardType,
            validator = validator
        )
    }

    fun getValue(key: String): String = fields[key]?.value?.value ?: ""

    fun setValue(key: String, value: String) {
        fields[key]?.value?.value = value
    }

    fun validate(): Boolean {
        var isValid = true
        fields.forEach { (_, field) ->
            val error = field.validator(field.value.value)
            field.errorMessage.value = error
            if (error != null) isValid = false
        }
        return isValid
    }

    fun clear() {
        fields.forEach { (_, field) ->
            field.value.value = ""
            field.errorMessage.value = null
        }
    }

    @Composable
    fun RenderForm(modifier: Modifier = Modifier) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            fields.forEach { (key, field) ->
                val errorMsg = field.errorMessage.value

                OutlinedTextField(
                    value = field.value.value,
                    onValueChange = {
                        field.value.value = it
                        field.errorMessage.value = null
                    },
                    label = { Text(field.label) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = field.keyboardType),
                    isError = errorMsg != null,
                    supportingText = {
                        if (errorMsg != null) {
                            Text(errorMsg, color = MaterialTheme.colorScheme.error)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun rememberFormAgent(): FormAgent {
    return remember { FormAgent() }
}

// Common validators
object FormValidators {
    fun required(message: String = "This field is required"): (String) -> String? = { value ->
        if (value.isBlank()) message else null
    }

    fun minLength(length: Int, message: String = "Minimum $length characters required"): (String) -> String? = { value ->
        if (value.length < length) message else null
    }

    fun number(message: String = "Must be a valid number"): (String) -> String? = { value ->
        if (value.toDoubleOrNull() == null) message else null
    }

    fun positiveNumber(message: String = "Must be a positive number"): (String) -> String? = { value ->
        val num = value.toDoubleOrNull()
        if (num == null || num <= 0) message else null
    }

    fun integer(message: String = "Must be a whole number"): (String) -> String? = { value ->
        if (value.toIntOrNull() == null) message else null
    }

    fun email(message: String = "Invalid email format"): (String) -> String? = { value ->
        if (!value.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))) message else null
    }

    fun combine(vararg validators: (String) -> String?): (String) -> String? = { value ->
        validators.firstNotNullOfOrNull { it(value) }
    }
}