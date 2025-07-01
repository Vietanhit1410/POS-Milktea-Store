package org.example.cnjava_milkteastore.frontend.getapi;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.cnjava_milkteastore.frontend.swingworker.ApiWorker;

import java.util.List;
import java.util.function.Consumer;

public class GetAPI<T> {

    private final TypeReference<List<T>> typeRef;
    private final Class<T> clazz;
    private final TypeReference<T> typeRefSingle ;
    private static final TypeReference<Void> VOID_TYPE = new TypeReference<Void>() {};

    public GetAPI(TypeReference<List<T>> typeRef, Class<T> clazz, TypeReference<T> typeRefSingle) {
        this.typeRef = typeRef;
        this.clazz = clazz;
        this.typeRefSingle = typeRefSingle;
    }

    private String getBaseUrl() {
        String simpleName = clazz.getSimpleName(); // InvoiceDetailDTO
        String endpoint = simpleName.toLowerCase().replace("dto", "") + "s"; // invoicedetails
        return "http://localhost:8080/api/" + endpoint;
    }

    public void fetchFromApi(Consumer<List<T>> callback) {
        String apiUrl = getBaseUrl();

        ApiWorker<List<T>> worker = new ApiWorker<>(
                apiUrl,
                "GET",
                null,
                typeRef,
                data -> {
                    if (data != null) callback.accept(data);
                },
                () -> System.out.println("🔄 Đang tải từ " + apiUrl + "..."),
                () -> System.out.println("✅ Đã tải xong từ " + apiUrl),
                e -> System.err.println("❌ Lỗi GET API: " + e.getMessage())
        );

        worker.execute();
    }

    public void postToApi(T dto, Consumer<T> callback) {
        String apiUrl = getBaseUrl();
        String jsonBody = ApiWorker.convertObjectToJson(dto);
        ApiWorker<T> worker = new ApiWorker<>(
                apiUrl,
                "POST",
                jsonBody,
                typeRefSingle,
                callback,
                () -> System.out.println("🔄 Đang gửi đối tượng..."),
                () -> System.out.println("✅ Đã gửi thành công!"),
                e -> System.err.println("❌ Lỗi POST: " + e.getMessage())
        );

        worker.execute();
    }

    public void postAllToApi(List<T> dtos, Consumer<List<T>> callback) {
        String apiUrl = getBaseUrl() + "/all";
        String jsonBody = ApiWorker.convertObjectToJson(dtos);

        ApiWorker<List<T>> worker = new ApiWorker<>(
                apiUrl,
                "POST",
                jsonBody,
                typeRef,
                callback,
                () -> System.out.println("🔄 Đang gửi danh sách..."),
                () -> System.out.println("✅ Gửi danh sách thành công!"),
                e -> System.err.println("❌ Lỗi POST ALL: " + e.getMessage())
        );

        worker.execute();
    }

    public void deleteFromApi(T dto, Runnable onSuccess) {
        String apiUrl = getBaseUrl();
        String jsonBody = ApiWorker.convertObjectToJson(dto);

        ApiWorker<Void> worker = new ApiWorker<>(
                apiUrl,
                "DELETE",
                jsonBody,
                VOID_TYPE,
                unused -> onSuccess.run(),
                () -> System.out.println("🗑️ Đang xóa đối tượng..."),
                () -> System.out.println("✅ Đã xóa!"),
                e -> System.err.println("❌ Lỗi DELETE: " + e.getMessage())
        );

        worker.execute();
    }

    public void deleteByIdFromApi(int id) {
        String apiUrl = getBaseUrl() + "/by-id?id=" + id;

        ApiWorker<Void> worker = new ApiWorker<>(
                apiUrl,
                "DELETE",
                null,
                VOID_TYPE,
                unused -> {},
                () -> System.out.println("🗑️ Đang xóa theo ID..."),
                () -> System.out.println("✅ Đã xóa theo ID!"),
                e -> System.err.println("❌ Lỗi DELETE by ID: " + e.getMessage())
        );

        worker.execute();
    }

    public void deleteAllFromApi(Runnable onSuccess) {
        String apiUrl = getBaseUrl() + "/all";

        ApiWorker<Void> worker = new ApiWorker<>(
                apiUrl,
                "DELETE",
                null,
                VOID_TYPE,
                unused -> onSuccess.run(),
                () -> System.out.println("🗑️ Đang xóa tất cả..."),
                () -> System.out.println("✅ Đã xóa tất cả!"),
                e -> System.err.println("❌ Lỗi DELETE ALL: " + e.getMessage())
        );

        worker.execute();
    }
}
