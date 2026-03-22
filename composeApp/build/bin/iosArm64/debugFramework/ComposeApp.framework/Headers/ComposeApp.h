#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class ComposeAppAppDatabase, ComposeAppAppDatabaseConstructor, ComposeAppKmp_room_coreBaseRepository<T, DAO>, ComposeAppKmp_room_coreDatabaseConfig, ComposeAppKmp_room_coreIosDatabaseFactory<T>, ComposeAppKotlinArray<T>, ComposeAppKotlinByteArray, ComposeAppKotlinByteIterator, ComposeAppKotlinException, ComposeAppKotlinIllegalStateException, ComposeAppKotlinRuntimeException, ComposeAppKotlinThrowable, ComposeAppLifecycle_viewmodelViewModel, ComposeAppProductDao_ImplCompanion, ComposeAppProductEntity, ComposeAppRoom_runtimeInvalidationTracker, ComposeAppRoom_runtimeMigration, ComposeAppRoom_runtimeRoomDatabase, ComposeAppRoom_runtimeRoomOpenDelegate, ComposeAppRoom_runtimeRoomOpenDelegateValidationResult, ComposeAppUserDao_ImplCompanion, ComposeAppUserEntity, ComposeAppUserRepository, UIViewController;

@protocol ComposeAppKmp_room_coreBaseDao, ComposeAppKmp_room_coreKmpDatabaseFactory, ComposeAppKotlinAutoCloseable, ComposeAppKotlinCoroutineContext, ComposeAppKotlinCoroutineContextElement, ComposeAppKotlinCoroutineContextKey, ComposeAppKotlinIterator, ComposeAppKotlinKAnnotatedElement, ComposeAppKotlinKClass, ComposeAppKotlinKClassifier, ComposeAppKotlinKDeclarationContainer, ComposeAppKotlinx_coroutines_coreCoroutineScope, ComposeAppKotlinx_coroutines_coreFlow, ComposeAppKotlinx_coroutines_coreFlowCollector, ComposeAppKotlinx_coroutines_coreSharedFlow, ComposeAppKotlinx_coroutines_coreStateFlow, ComposeAppPlatform, ComposeAppProductDao, ComposeAppRoom_runtimeAutoMigrationSpec, ComposeAppRoom_runtimeRoomDatabaseConstructor, ComposeAppRoom_runtimeRoomOpenDelegateMarker, ComposeAppSqliteSQLiteConnection, ComposeAppSqliteSQLiteStatement, ComposeAppUserDao;

NS_ASSUME_NONNULL_BEGIN
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunknown-warning-option"
#pragma clang diagnostic ignored "-Wincompatible-property-type"
#pragma clang diagnostic ignored "-Wnullability"

#pragma push_macro("_Nullable_result")
#if !__has_feature(nullability_nullable_result)
#undef _Nullable_result
#define _Nullable_result _Nullable
#endif

__attribute__((swift_name("KotlinBase")))
@interface ComposeAppBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end

@interface ComposeAppBase (ComposeAppBaseCopying) <NSCopying>
@end

__attribute__((swift_name("KotlinMutableSet")))
@interface ComposeAppMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end

__attribute__((swift_name("KotlinMutableDictionary")))
@interface ComposeAppMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end

@interface NSError (NSErrorComposeAppKotlinException)
@property (readonly) id _Nullable kotlinException;
@end

__attribute__((swift_name("KotlinNumber")))
@interface ComposeAppNumber : NSNumber
- (instancetype)initWithChar:(char)value __attribute__((unavailable));
- (instancetype)initWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
- (instancetype)initWithShort:(short)value __attribute__((unavailable));
- (instancetype)initWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
- (instancetype)initWithInt:(int)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
- (instancetype)initWithLong:(long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
- (instancetype)initWithLongLong:(long long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
- (instancetype)initWithFloat:(float)value __attribute__((unavailable));
- (instancetype)initWithDouble:(double)value __attribute__((unavailable));
- (instancetype)initWithBool:(BOOL)value __attribute__((unavailable));
- (instancetype)initWithInteger:(NSInteger)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
+ (instancetype)numberWithChar:(char)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
+ (instancetype)numberWithShort:(short)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
+ (instancetype)numberWithInt:(int)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
+ (instancetype)numberWithLong:(long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
+ (instancetype)numberWithLongLong:(long long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
+ (instancetype)numberWithFloat:(float)value __attribute__((unavailable));
+ (instancetype)numberWithDouble:(double)value __attribute__((unavailable));
+ (instancetype)numberWithBool:(BOOL)value __attribute__((unavailable));
+ (instancetype)numberWithInteger:(NSInteger)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
@end

__attribute__((swift_name("KotlinByte")))
@interface ComposeAppByte : ComposeAppNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end

__attribute__((swift_name("KotlinUByte")))
@interface ComposeAppUByte : ComposeAppNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end

__attribute__((swift_name("KotlinShort")))
@interface ComposeAppShort : ComposeAppNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end

__attribute__((swift_name("KotlinUShort")))
@interface ComposeAppUShort : ComposeAppNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end

__attribute__((swift_name("KotlinInt")))
@interface ComposeAppInt : ComposeAppNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end

__attribute__((swift_name("KotlinUInt")))
@interface ComposeAppUInt : ComposeAppNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end

__attribute__((swift_name("KotlinLong")))
@interface ComposeAppLong : ComposeAppNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end

__attribute__((swift_name("KotlinULong")))
@interface ComposeAppULong : ComposeAppNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end

__attribute__((swift_name("KotlinFloat")))
@interface ComposeAppFloat : ComposeAppNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end

__attribute__((swift_name("KotlinDouble")))
@interface ComposeAppDouble : ComposeAppNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end

__attribute__((swift_name("KotlinBoolean")))
@interface ComposeAppBoolean : ComposeAppNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Greeting")))
@interface ComposeAppGreeting : ComposeAppBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (NSString *)greet __attribute__((swift_name("greet()")));
@end

__attribute__((swift_name("Platform")))
@protocol ComposeAppPlatform
@required
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IOSPlatform")))
@interface ComposeAppIOSPlatform : ComposeAppBase <ComposeAppPlatform>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end

__attribute__((swift_name("Kmp_room_coreBaseDao")))
@protocol ComposeAppKmp_room_coreBaseDao
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)deleteEntity:(id _Nullable)entity completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("delete(entity:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)deleteAllEntities:(NSArray<id> *)entities completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("deleteAll(entities:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)insertEntity:(id _Nullable)entity completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("insert(entity:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)insertAllEntities:(NSArray<id> *)entities completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("insertAll(entities:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)updateEntity:(id _Nullable)entity completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("update(entity:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)updateAllEntities:(NSArray<id> *)entities completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("updateAll(entities:completionHandler:)")));
@end

__attribute__((swift_name("ProductDao")))
@protocol ComposeAppProductDao <ComposeAppKmp_room_coreBaseDao>
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)deleteAllProductsWithCompletionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("deleteAllProducts(completionHandler:)")));
- (id<ComposeAppKotlinx_coroutines_coreFlow>)getAllProducts __attribute__((swift_name("getAllProducts()")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)getProductByIdId:(int32_t)id completionHandler:(void (^)(ComposeAppProductEntity * _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("getProductById(id:completionHandler:)")));
- (id<ComposeAppKotlinx_coroutines_coreFlow>)getProductsByPriceMaxPrice:(double)maxPrice __attribute__((swift_name("getProductsByPrice(maxPrice:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ProductDao_Impl")))
@interface ComposeAppProductDao_Impl : ComposeAppBase <ComposeAppProductDao>
- (instancetype)initWith__db:(ComposeAppRoom_runtimeRoomDatabase *)__db __attribute__((swift_name("init(__db:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) ComposeAppProductDao_ImplCompanion *companion __attribute__((swift_name("companion")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)deleteEntity:(ComposeAppProductEntity *)entity completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("delete(entity:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)deleteAllEntities:(NSArray<ComposeAppProductEntity *> *)entities completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("deleteAll(entities:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)deleteAllProductsWithCompletionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("deleteAllProducts(completionHandler:)")));
- (id<ComposeAppKotlinx_coroutines_coreFlow>)getAllProducts __attribute__((swift_name("getAllProducts()")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)getProductByIdId:(int32_t)id completionHandler:(void (^)(ComposeAppProductEntity * _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("getProductById(id:completionHandler:)")));
- (id<ComposeAppKotlinx_coroutines_coreFlow>)getProductsByPriceMaxPrice:(double)maxPrice __attribute__((swift_name("getProductsByPrice(maxPrice:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)insertEntity:(ComposeAppProductEntity *)entity completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("insert(entity:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)insertAllEntities:(NSArray<ComposeAppProductEntity *> *)entities completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("insertAll(entities:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)updateEntity:(ComposeAppProductEntity *)entity completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("update(entity:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)updateAllEntities:(NSArray<ComposeAppProductEntity *> *)entities completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("updateAll(entities:completionHandler:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ProductDao_Impl.Companion")))
@interface ComposeAppProductDao_ImplCompanion : ComposeAppBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) ComposeAppProductDao_ImplCompanion *shared __attribute__((swift_name("shared")));
- (NSArray<id<ComposeAppKotlinKClass>> *)getRequiredConverters __attribute__((swift_name("getRequiredConverters()")));
@end

__attribute__((swift_name("UserDao")))
@protocol ComposeAppUserDao <ComposeAppKmp_room_coreBaseDao>
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)deleteAllUsersWithCompletionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("deleteAllUsers(completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)getUserByIdId:(int32_t)id completionHandler:(void (^)(ComposeAppUserEntity * _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("getUserById(id:completionHandler:)")));
- (id<ComposeAppKotlinx_coroutines_coreFlow>)getUsers __attribute__((swift_name("getUsers()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UserDao_Impl")))
@interface ComposeAppUserDao_Impl : ComposeAppBase <ComposeAppUserDao>
- (instancetype)initWith__db:(ComposeAppRoom_runtimeRoomDatabase *)__db __attribute__((swift_name("init(__db:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) ComposeAppUserDao_ImplCompanion *companion __attribute__((swift_name("companion")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)deleteEntity:(ComposeAppUserEntity *)entity completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("delete(entity:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)deleteAllEntities:(NSArray<ComposeAppUserEntity *> *)entities completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("deleteAll(entities:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)deleteAllUsersWithCompletionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("deleteAllUsers(completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)getUserByIdId:(int32_t)id completionHandler:(void (^)(ComposeAppUserEntity * _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("getUserById(id:completionHandler:)")));
- (id<ComposeAppKotlinx_coroutines_coreFlow>)getUsers __attribute__((swift_name("getUsers()")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)insertEntity:(ComposeAppUserEntity *)entity completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("insert(entity:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)insertAllEntities:(NSArray<ComposeAppUserEntity *> *)entities completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("insertAll(entities:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)updateEntity:(ComposeAppUserEntity *)entity completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("update(entity:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)updateAllEntities:(NSArray<ComposeAppUserEntity *> *)entities completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("updateAll(entities:completionHandler:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UserDao_Impl.Companion")))
@interface ComposeAppUserDao_ImplCompanion : ComposeAppBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) ComposeAppUserDao_ImplCompanion *shared __attribute__((swift_name("shared")));
- (NSArray<id<ComposeAppKotlinKClass>> *)getRequiredConverters __attribute__((swift_name("getRequiredConverters()")));
@end

__attribute__((swift_name("Room_runtimeRoomDatabase")))
@interface ComposeAppRoom_runtimeRoomDatabase : ComposeAppBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (void)close __attribute__((swift_name("close()")));

/**
 * @note annotations
 *   androidx.annotation.RestrictTo(value=[Scope.LIBRARY_GROUP_PREFIX])
*/
- (NSArray<ComposeAppRoom_runtimeMigration *> *)createAutoMigrationsAutoMigrationSpecs:(NSDictionary<id<ComposeAppKotlinKClass>, id<ComposeAppRoom_runtimeAutoMigrationSpec>> *)autoMigrationSpecs __attribute__((swift_name("createAutoMigrations(autoMigrationSpecs:)")));

/**
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (ComposeAppRoom_runtimeInvalidationTracker *)createInvalidationTracker __attribute__((swift_name("createInvalidationTracker()")));

/**
 * @note annotations
 *   androidx.annotation.RestrictTo(value=[Scope.LIBRARY_GROUP_PREFIX])
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (id<ComposeAppRoom_runtimeRoomOpenDelegateMarker>)createOpenDelegate __attribute__((swift_name("createOpenDelegate()")));

/**
 * @note annotations
 *   androidx.annotation.RestrictTo(value=[Scope.LIBRARY_GROUP])
*/
- (id<ComposeAppKotlinx_coroutines_coreCoroutineScope>)getCoroutineScope __attribute__((swift_name("getCoroutineScope()")));

/**
 * @note annotations
 *   androidx.annotation.RestrictTo(value=[Scope.LIBRARY_GROUP_PREFIX])
*/
- (NSSet<id<ComposeAppKotlinKClass>> *)getRequiredAutoMigrationSpecClasses __attribute__((swift_name("getRequiredAutoMigrationSpecClasses()")));

/**
 * @note annotations
 *   androidx.annotation.RestrictTo(value=[Scope.LIBRARY_GROUP_PREFIX])
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (NSDictionary<id<ComposeAppKotlinKClass>, NSArray<id<ComposeAppKotlinKClass>> *> *)getRequiredTypeConverterClasses __attribute__((swift_name("getRequiredTypeConverterClasses()")));

/**
 * @note annotations
 *   androidx.annotation.RestrictTo(value=[Scope.LIBRARY_GROUP_PREFIX])
*/
- (id)getTypeConverterKlass:(id<ComposeAppKotlinKClass>)klass __attribute__((swift_name("getTypeConverter(klass:)")));

/**
 * @note annotations
 *   androidx.annotation.RestrictTo(value=[Scope.LIBRARY_GROUP_PREFIX])
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (void)internalInitInvalidationTrackerConnection:(id<ComposeAppSqliteSQLiteConnection>)connection __attribute__((swift_name("internalInitInvalidationTracker(connection:)")));
@property (readonly) ComposeAppRoom_runtimeInvalidationTracker *invalidationTracker __attribute__((swift_name("invalidationTracker")));
@end

__attribute__((swift_name("AppDatabase")))
@interface ComposeAppAppDatabase : ComposeAppRoom_runtimeRoomDatabase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (id<ComposeAppProductDao>)productDao __attribute__((swift_name("productDao()")));
- (id<ComposeAppUserDao>)userDao __attribute__((swift_name("userDao()")));
@end

__attribute__((swift_name("Room_runtimeRoomDatabaseConstructor")))
@protocol ComposeAppRoom_runtimeRoomDatabaseConstructor
@required
- (ComposeAppRoom_runtimeRoomDatabase *)initialize __attribute__((swift_name("initialize()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AppDatabaseConstructor")))
@interface ComposeAppAppDatabaseConstructor : ComposeAppBase <ComposeAppRoom_runtimeRoomDatabaseConstructor>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)appDatabaseConstructor __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) ComposeAppAppDatabaseConstructor *shared __attribute__((swift_name("shared")));
- (ComposeAppAppDatabase *)initialize __attribute__((swift_name("initialize()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AppDatabase_Impl")))
@interface ComposeAppAppDatabase_Impl : ComposeAppAppDatabase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (NSArray<ComposeAppRoom_runtimeMigration *> *)createAutoMigrationsAutoMigrationSpecs:(NSDictionary<id<ComposeAppKotlinKClass>, id<ComposeAppRoom_runtimeAutoMigrationSpec>> *)autoMigrationSpecs __attribute__((swift_name("createAutoMigrations(autoMigrationSpecs:)")));

/**
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (ComposeAppRoom_runtimeInvalidationTracker *)createInvalidationTracker __attribute__((swift_name("createInvalidationTracker()")));

/**
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (ComposeAppRoom_runtimeRoomOpenDelegate *)createOpenDelegate __attribute__((swift_name("createOpenDelegate()")));
- (NSSet<id<ComposeAppKotlinKClass>> *)getRequiredAutoMigrationSpecClasses __attribute__((swift_name("getRequiredAutoMigrationSpecClasses()")));

/**
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (NSDictionary<id<ComposeAppKotlinKClass>, NSArray<id<ComposeAppKotlinKClass>> *> *)getRequiredTypeConverterClasses __attribute__((swift_name("getRequiredTypeConverterClasses()")));
- (id<ComposeAppProductDao>)productDao __attribute__((swift_name("productDao()")));
- (id<ComposeAppUserDao>)userDao __attribute__((swift_name("userDao()")));
@end

__attribute__((swift_name("Kmp_room_coreKmpDatabaseFactory")))
@protocol ComposeAppKmp_room_coreKmpDatabaseFactory
@required
- (ComposeAppRoom_runtimeRoomDatabase *)createDatabaseName:(NSString *)name migrations:(NSArray<ComposeAppRoom_runtimeMigration *> *)migrations __attribute__((swift_name("createDatabase(name:migrations:)")));
@end

__attribute__((swift_name("Kmp_room_coreIosDatabaseFactory")))
@interface ComposeAppKmp_room_coreIosDatabaseFactory<T> : ComposeAppBase <ComposeAppKmp_room_coreKmpDatabaseFactory>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));

/**
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (ComposeAppRoom_runtimeRoomDatabase *)buildDatabaseConfig:(ComposeAppKmp_room_coreDatabaseConfig *)config __attribute__((swift_name("buildDatabase(config:)")));

/**
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (ComposeAppRoom_runtimeRoomDatabase *)buildDatabaseName:(NSString *)name version:(int32_t)version migrations:(NSArray<ComposeAppRoom_runtimeMigration *> *)migrations __attribute__((swift_name("buildDatabase(name:version:migrations:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DatabaseFactory")))
@interface ComposeAppDatabaseFactory : ComposeAppKmp_room_coreIosDatabaseFactory<ComposeAppAppDatabase *>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (ComposeAppAppDatabase *)createDatabase __attribute__((swift_name("createDatabase()")));
- (ComposeAppAppDatabase *)createDatabaseName:(NSString *)name migrations:(NSArray<ComposeAppRoom_runtimeMigration *> *)migrations __attribute__((swift_name("createDatabase(name:migrations:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ProductEntity")))
@interface ComposeAppProductEntity : ComposeAppBase
- (instancetype)initWithId:(int32_t)id name:(NSString *)name price:(double)price quantity:(int32_t)quantity __attribute__((swift_name("init(id:name:price:quantity:)"))) __attribute__((objc_designated_initializer));
- (ComposeAppProductEntity *)doCopyId:(int32_t)id name:(NSString *)name price:(double)price quantity:(int32_t)quantity __attribute__((swift_name("doCopy(id:name:price:quantity:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t id __attribute__((swift_name("id")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) double price __attribute__((swift_name("price")));
@property (readonly) int32_t quantity __attribute__((swift_name("quantity")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UserEntity")))
@interface ComposeAppUserEntity : ComposeAppBase
- (instancetype)initWithId:(int32_t)id name:(NSString *)name __attribute__((swift_name("init(id:name:)"))) __attribute__((objc_designated_initializer));
- (ComposeAppUserEntity *)doCopyId:(int32_t)id name:(NSString *)name __attribute__((swift_name("doCopy(id:name:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t id __attribute__((swift_name("id")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end

__attribute__((swift_name("Kmp_room_coreBaseRepository")))
@interface ComposeAppKmp_room_coreBaseRepository<T, DAO> : ComposeAppBase
- (instancetype)initWithDao:(DAO)dao __attribute__((swift_name("init(dao:)"))) __attribute__((objc_designated_initializer));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)addEntity:(T _Nullable)entity completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("add(entity:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)addAllEntities:(NSArray<id> *)entities completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("addAll(entities:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)deleteEntity:(T _Nullable)entity completionHandler_:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("delete(entity:completionHandler_:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)deleteAllEntities:(NSArray<id> *)entities completionHandler_:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("deleteAll(entities:completionHandler_:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)updateEntity:(T _Nullable)entity completionHandler_:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("update(entity:completionHandler_:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)updateAllEntities:(NSArray<id> *)entities completionHandler_:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("updateAll(entities:completionHandler_:)")));

/**
 * @note This property has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
@property (readonly) DAO dao __attribute__((swift_name("dao")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ProductRepository")))
@interface ComposeAppProductRepository : ComposeAppKmp_room_coreBaseRepository<ComposeAppProductEntity *, id<ComposeAppProductDao>>
- (instancetype)initWithDatabase:(ComposeAppAppDatabase *)database __attribute__((swift_name("init(database:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithDao:(id<ComposeAppKmp_room_coreBaseDao>)dao __attribute__((swift_name("init(dao:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)addProductName:(NSString *)name price:(double)price quantity:(int32_t)quantity completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("addProduct(name:price:quantity:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)deleteAllProductsWithCompletionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("deleteAllProducts(completionHandler:)")));
- (id<ComposeAppKotlinx_coroutines_coreFlow>)getAllProducts __attribute__((swift_name("getAllProducts()")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)getProductByIdId:(int32_t)id completionHandler:(void (^)(ComposeAppProductEntity * _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("getProductById(id:completionHandler:)")));
- (id<ComposeAppKotlinx_coroutines_coreFlow>)getProductsByPriceMaxPrice:(double)maxPrice __attribute__((swift_name("getProductsByPrice(maxPrice:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)updateProductQuantityProduct:(ComposeAppProductEntity *)product newQuantity:(int32_t)newQuantity completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("updateProductQuantity(product:newQuantity:completionHandler:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UserRepository")))
@interface ComposeAppUserRepository : ComposeAppKmp_room_coreBaseRepository<ComposeAppUserEntity *, id<ComposeAppUserDao>>
- (instancetype)initWithDatabase:(ComposeAppAppDatabase *)database __attribute__((swift_name("init(database:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithDao:(id<ComposeAppKmp_room_coreBaseDao>)dao __attribute__((swift_name("init(dao:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)addUserName:(NSString *)name completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("addUser(name:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)deleteAllUsersWithCompletionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("deleteAllUsers(completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)getUserByIdId:(int32_t)id completionHandler:(void (^)(ComposeAppUserEntity * _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("getUserById(id:completionHandler:)")));
- (id<ComposeAppKotlinx_coroutines_coreFlow>)getUsers __attribute__((swift_name("getUsers()")));
@end

__attribute__((swift_name("Lifecycle_viewmodelViewModel")))
@interface ComposeAppLifecycle_viewmodelViewModel : ComposeAppBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithCloseables:(ComposeAppKotlinArray<id<ComposeAppKotlinAutoCloseable>> *)closeables __attribute__((swift_name("init(closeables:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithViewModelScope:(id<ComposeAppKotlinx_coroutines_coreCoroutineScope>)viewModelScope __attribute__((swift_name("init(viewModelScope:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithViewModelScope:(id<ComposeAppKotlinx_coroutines_coreCoroutineScope>)viewModelScope closeables:(ComposeAppKotlinArray<id<ComposeAppKotlinAutoCloseable>> *)closeables __attribute__((swift_name("init(viewModelScope:closeables:)"))) __attribute__((objc_designated_initializer));
- (void)addCloseableCloseable:(id<ComposeAppKotlinAutoCloseable>)closeable __attribute__((swift_name("addCloseable(closeable:)")));
- (void)addCloseableKey:(NSString *)key closeable:(id<ComposeAppKotlinAutoCloseable>)closeable __attribute__((swift_name("addCloseable(key:closeable:)")));
- (id<ComposeAppKotlinAutoCloseable> _Nullable)getCloseableKey:(NSString *)key __attribute__((swift_name("getCloseable(key:)")));

/**
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (void)onCleared __attribute__((swift_name("onCleared()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UserViewModel")))
@interface ComposeAppUserViewModel : ComposeAppLifecycle_viewmodelViewModel
- (instancetype)initWithRepository:(ComposeAppUserRepository *)repository __attribute__((swift_name("init(repository:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (instancetype)initWithCloseables:(ComposeAppKotlinArray<id<ComposeAppKotlinAutoCloseable>> *)closeables __attribute__((swift_name("init(closeables:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithViewModelScope:(id<ComposeAppKotlinx_coroutines_coreCoroutineScope>)viewModelScope __attribute__((swift_name("init(viewModelScope:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithViewModelScope:(id<ComposeAppKotlinx_coroutines_coreCoroutineScope>)viewModelScope closeables:(ComposeAppKotlinArray<id<ComposeAppKotlinAutoCloseable>> *)closeables __attribute__((swift_name("init(viewModelScope:closeables:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (void)addUserName:(NSString *)name __attribute__((swift_name("addUser(name:)")));
@property (readonly) id<ComposeAppKotlinx_coroutines_coreStateFlow> users __attribute__((swift_name("users")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MainViewControllerKt")))
@interface ComposeAppMainViewControllerKt : ComposeAppBase
+ (UIViewController *)MainViewController __attribute__((swift_name("MainViewController()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Platform_iosKt")))
@interface ComposeAppPlatform_iosKt : ComposeAppBase
+ (id<ComposeAppPlatform>)getPlatform __attribute__((swift_name("getPlatform()")));
@end

__attribute__((swift_name("KotlinThrowable")))
@interface ComposeAppKotlinThrowable : ComposeAppBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(ComposeAppKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(ComposeAppKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));

/**
 * @note annotations
 *   kotlin.experimental.ExperimentalNativeApi
*/
- (ComposeAppKotlinArray<NSString *> *)getStackTrace __attribute__((swift_name("getStackTrace()")));
- (void)printStackTrace __attribute__((swift_name("printStackTrace()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) ComposeAppKotlinThrowable * _Nullable cause __attribute__((swift_name("cause")));
@property (readonly) NSString * _Nullable message __attribute__((swift_name("message")));
- (NSError *)asError __attribute__((swift_name("asError()")));
@end

__attribute__((swift_name("KotlinException")))
@interface ComposeAppKotlinException : ComposeAppKotlinThrowable
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(ComposeAppKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(ComposeAppKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("KotlinRuntimeException")))
@interface ComposeAppKotlinRuntimeException : ComposeAppKotlinException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(ComposeAppKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(ComposeAppKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("KotlinIllegalStateException")))
@interface ComposeAppKotlinIllegalStateException : ComposeAppKotlinRuntimeException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(ComposeAppKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(ComposeAppKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.4")
*/
__attribute__((swift_name("KotlinCancellationException")))
@interface ComposeAppKotlinCancellationException : ComposeAppKotlinIllegalStateException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(ComposeAppKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(ComposeAppKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreFlow")))
@protocol ComposeAppKotlinx_coroutines_coreFlow
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)collectCollector:(id<ComposeAppKotlinx_coroutines_coreFlowCollector>)collector completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("collect(collector:completionHandler:)")));
@end

__attribute__((swift_name("KotlinKDeclarationContainer")))
@protocol ComposeAppKotlinKDeclarationContainer
@required
@end

__attribute__((swift_name("KotlinKAnnotatedElement")))
@protocol ComposeAppKotlinKAnnotatedElement
@required
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.1")
*/
__attribute__((swift_name("KotlinKClassifier")))
@protocol ComposeAppKotlinKClassifier
@required
@end

__attribute__((swift_name("KotlinKClass")))
@protocol ComposeAppKotlinKClass <ComposeAppKotlinKDeclarationContainer, ComposeAppKotlinKAnnotatedElement, ComposeAppKotlinKClassifier>
@required

/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.1")
*/
- (BOOL)isInstanceValue:(id _Nullable)value __attribute__((swift_name("isInstance(value:)")));
@property (readonly) NSString * _Nullable qualifiedName __attribute__((swift_name("qualifiedName")));
@property (readonly) NSString * _Nullable simpleName __attribute__((swift_name("simpleName")));
@end

__attribute__((swift_name("Room_runtimeMigration")))
@interface ComposeAppRoom_runtimeMigration : ComposeAppBase
- (instancetype)initWithStartVersion:(int32_t)startVersion endVersion:(int32_t)endVersion __attribute__((swift_name("init(startVersion:endVersion:)"))) __attribute__((objc_designated_initializer));
- (void)migrateConnection:(id<ComposeAppSqliteSQLiteConnection>)connection __attribute__((swift_name("migrate(connection:)")));
@property (readonly) int32_t endVersion __attribute__((swift_name("endVersion")));
@property (readonly) int32_t startVersion __attribute__((swift_name("startVersion")));
@end

__attribute__((swift_name("Room_runtimeAutoMigrationSpec")))
@protocol ComposeAppRoom_runtimeAutoMigrationSpec
@required
- (void)onPostMigrateConnection:(id<ComposeAppSqliteSQLiteConnection>)connection __attribute__((swift_name("onPostMigrate(connection:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Room_runtimeInvalidationTracker")))
@interface ComposeAppRoom_runtimeInvalidationTracker : ComposeAppBase

/**
 * @note annotations
 *   androidx.annotation.RestrictTo(value=[Scope.LIBRARY_GROUP_PREFIX])
*/
- (instancetype)initWithDatabase:(ComposeAppRoom_runtimeRoomDatabase *)database shadowTablesMap:(NSDictionary<NSString *, NSString *> *)shadowTablesMap viewTables:(NSDictionary<NSString *, NSSet<NSString *> *> *)viewTables tableNames:(ComposeAppKotlinArray<NSString *> *)tableNames __attribute__((swift_name("init(database:shadowTablesMap:viewTables:tableNames:)"))) __attribute__((objc_designated_initializer));

/**
 * @note annotations
 *   kotlin.jvm.JvmOverloads
*/
- (id<ComposeAppKotlinx_coroutines_coreFlow>)createFlowTables:(ComposeAppKotlinArray<NSString *> *)tables emitInitialState:(BOOL)emitInitialState __attribute__((swift_name("createFlow(tables:emitInitialState:)")));

/**
 * @note annotations
 *   androidx.annotation.RestrictTo(value=[Scope.LIBRARY_GROUP])
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)refreshTables:(ComposeAppKotlinArray<NSString *> *)tables completionHandler:(void (^)(ComposeAppBoolean * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("refresh(tables:completionHandler:)")));
- (void)refreshAsync __attribute__((swift_name("refreshAsync()")));
@end

__attribute__((swift_name("Room_runtimeRoomOpenDelegateMarker")))
@protocol ComposeAppRoom_runtimeRoomOpenDelegateMarker
@required
@end

__attribute__((swift_name("Kotlinx_coroutines_coreCoroutineScope")))
@protocol ComposeAppKotlinx_coroutines_coreCoroutineScope
@required
@property (readonly) id<ComposeAppKotlinCoroutineContext> coroutineContext __attribute__((swift_name("coroutineContext")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="2.0")
*/
__attribute__((swift_name("KotlinAutoCloseable")))
@protocol ComposeAppKotlinAutoCloseable
@required
- (void)close __attribute__((swift_name("close()")));
@end

__attribute__((swift_name("SqliteSQLiteConnection")))
@protocol ComposeAppSqliteSQLiteConnection <ComposeAppKotlinAutoCloseable>
@required
- (id<ComposeAppSqliteSQLiteStatement>)prepareSql:(NSString *)sql __attribute__((swift_name("prepare(sql:)")));
@end


/**
 * @note annotations
 *   androidx.annotation.RestrictTo(value=[Scope.LIBRARY_GROUP_PREFIX])
*/
__attribute__((swift_name("Room_runtimeRoomOpenDelegate")))
@interface ComposeAppRoom_runtimeRoomOpenDelegate : ComposeAppBase <ComposeAppRoom_runtimeRoomOpenDelegateMarker>
- (instancetype)initWithVersion:(int32_t)version identityHash:(NSString *)identityHash legacyIdentityHash:(NSString *)legacyIdentityHash __attribute__((swift_name("init(version:identityHash:legacyIdentityHash:)"))) __attribute__((objc_designated_initializer));
- (void)createAllTablesConnection:(id<ComposeAppSqliteSQLiteConnection>)connection __attribute__((swift_name("createAllTables(connection:)")));
- (void)dropAllTablesConnection:(id<ComposeAppSqliteSQLiteConnection>)connection __attribute__((swift_name("dropAllTables(connection:)")));
- (void)onCreateConnection:(id<ComposeAppSqliteSQLiteConnection>)connection __attribute__((swift_name("onCreate(connection:)")));
- (void)onOpenConnection:(id<ComposeAppSqliteSQLiteConnection>)connection __attribute__((swift_name("onOpen(connection:)")));
- (void)onPostMigrateConnection:(id<ComposeAppSqliteSQLiteConnection>)connection __attribute__((swift_name("onPostMigrate(connection:)")));
- (void)onPreMigrateConnection:(id<ComposeAppSqliteSQLiteConnection>)connection __attribute__((swift_name("onPreMigrate(connection:)")));
- (ComposeAppRoom_runtimeRoomOpenDelegateValidationResult *)onValidateSchemaConnection:(id<ComposeAppSqliteSQLiteConnection>)connection __attribute__((swift_name("onValidateSchema(connection:)")));
@property (readonly) NSString *identityHash __attribute__((swift_name("identityHash")));
@property (readonly) NSString *legacyIdentityHash __attribute__((swift_name("legacyIdentityHash")));
@property (readonly) int32_t version __attribute__((swift_name("version")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kmp_room_coreDatabaseConfig")))
@interface ComposeAppKmp_room_coreDatabaseConfig : ComposeAppBase
- (instancetype)initWithName:(NSString *)name version:(int32_t)version enableLogging:(BOOL)enableLogging migrations:(NSArray<ComposeAppRoom_runtimeMigration *> *)migrations __attribute__((swift_name("init(name:version:enableLogging:migrations:)"))) __attribute__((objc_designated_initializer));
- (ComposeAppKmp_room_coreDatabaseConfig *)doCopyName:(NSString *)name version:(int32_t)version enableLogging:(BOOL)enableLogging migrations:(NSArray<ComposeAppRoom_runtimeMigration *> *)migrations __attribute__((swift_name("doCopy(name:version:enableLogging:migrations:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) BOOL enableLogging __attribute__((swift_name("enableLogging")));
@property (readonly) NSArray<ComposeAppRoom_runtimeMigration *> *migrations __attribute__((swift_name("migrations")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) int32_t version __attribute__((swift_name("version")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinArray")))
@interface ComposeAppKotlinArray<T> : ComposeAppBase
+ (instancetype)arrayWithSize:(int32_t)size init:(T _Nullable (^)(ComposeAppInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (T _Nullable)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (id<ComposeAppKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(T _Nullable)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreSharedFlow")))
@protocol ComposeAppKotlinx_coroutines_coreSharedFlow <ComposeAppKotlinx_coroutines_coreFlow>
@required
@property (readonly) NSArray<id> *replayCache __attribute__((swift_name("replayCache")));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreStateFlow")))
@protocol ComposeAppKotlinx_coroutines_coreStateFlow <ComposeAppKotlinx_coroutines_coreSharedFlow>
@required
@property (readonly) id _Nullable value __attribute__((swift_name("value")));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreFlowCollector")))
@protocol ComposeAppKotlinx_coroutines_coreFlowCollector
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)emitValue:(id _Nullable)value completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("emit(value:completionHandler:)")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.3")
*/
__attribute__((swift_name("KotlinCoroutineContext")))
@protocol ComposeAppKotlinCoroutineContext
@required
- (id _Nullable)foldInitial:(id _Nullable)initial operation:(id _Nullable (^)(id _Nullable, id<ComposeAppKotlinCoroutineContextElement>))operation __attribute__((swift_name("fold(initial:operation:)")));
- (id<ComposeAppKotlinCoroutineContextElement> _Nullable)getKey:(id<ComposeAppKotlinCoroutineContextKey>)key __attribute__((swift_name("get(key:)")));
- (id<ComposeAppKotlinCoroutineContext>)minusKeyKey:(id<ComposeAppKotlinCoroutineContextKey>)key __attribute__((swift_name("minusKey(key:)")));
- (id<ComposeAppKotlinCoroutineContext>)plusContext:(id<ComposeAppKotlinCoroutineContext>)context __attribute__((swift_name("plus(context:)")));
@end

__attribute__((swift_name("SqliteSQLiteStatement")))
@protocol ComposeAppSqliteSQLiteStatement <ComposeAppKotlinAutoCloseable>
@required

/**
 * @param index annotations androidx.annotation.IntRange(from=1.toLong())
*/
- (void)bindBlobIndex:(int32_t)index value:(ComposeAppKotlinByteArray *)value __attribute__((swift_name("bindBlob(index:value:)")));

/**
 * @param index annotations androidx.annotation.IntRange(from=1.toLong())
*/
- (void)bindBooleanIndex:(int32_t)index value:(BOOL)value __attribute__((swift_name("bindBoolean(index:value:)")));

/**
 * @param index annotations androidx.annotation.IntRange(from=1.toLong())
*/
- (void)bindDoubleIndex:(int32_t)index value:(double)value __attribute__((swift_name("bindDouble(index:value:)")));

/**
 * @param index annotations androidx.annotation.IntRange(from=1.toLong())
*/
- (void)bindFloatIndex:(int32_t)index value:(float)value __attribute__((swift_name("bindFloat(index:value:)")));

/**
 * @param index annotations androidx.annotation.IntRange(from=1.toLong())
*/
- (void)bindIntIndex:(int32_t)index value:(int32_t)value __attribute__((swift_name("bindInt(index:value:)")));

/**
 * @param index annotations androidx.annotation.IntRange(from=1.toLong())
*/
- (void)bindLongIndex:(int32_t)index value:(int64_t)value __attribute__((swift_name("bindLong(index:value:)")));

/**
 * @param index annotations androidx.annotation.IntRange(from=1.toLong())
*/
- (void)bindNullIndex:(int32_t)index __attribute__((swift_name("bindNull(index:)")));

/**
 * @param index annotations androidx.annotation.IntRange(from=1.toLong())
*/
- (void)bindTextIndex:(int32_t)index value:(NSString *)value __attribute__((swift_name("bindText(index:value:)")));
- (void)clearBindings __attribute__((swift_name("clearBindings()")));

/**
 * @param index annotations androidx.annotation.IntRange(from=0.toLong())
*/
- (ComposeAppKotlinByteArray *)getBlobIndex:(int32_t)index __attribute__((swift_name("getBlob(index:)")));

/**
 * @param index annotations androidx.annotation.IntRange(from=0.toLong())
*/
- (BOOL)getBooleanIndex:(int32_t)index __attribute__((swift_name("getBoolean(index:)")));
- (int32_t)getColumnCount __attribute__((swift_name("getColumnCount()")));

/**
 * @param index annotations androidx.annotation.IntRange(from=0.toLong())
*/
- (NSString *)getColumnNameIndex:(int32_t)index __attribute__((swift_name("getColumnName(index:)")));
- (NSArray<NSString *> *)getColumnNames __attribute__((swift_name("getColumnNames()")));

/**
 * @param index annotations androidx.annotation.IntRange(from=0.toLong())
*/
- (int32_t)getColumnTypeIndex:(int32_t)index __attribute__((swift_name("getColumnType(index:)")));

/**
 * @param index annotations androidx.annotation.IntRange(from=0.toLong())
*/
- (double)getDoubleIndex:(int32_t)index __attribute__((swift_name("getDouble(index:)")));

/**
 * @param index annotations androidx.annotation.IntRange(from=0.toLong())
*/
- (float)getFloatIndex:(int32_t)index __attribute__((swift_name("getFloat(index:)")));

/**
 * @param index annotations androidx.annotation.IntRange(from=0.toLong())
*/
- (int32_t)getIntIndex:(int32_t)index __attribute__((swift_name("getInt(index:)")));

/**
 * @param index annotations androidx.annotation.IntRange(from=0.toLong())
*/
- (int64_t)getLongIndex:(int32_t)index __attribute__((swift_name("getLong(index:)")));

/**
 * @param index annotations androidx.annotation.IntRange(from=0.toLong())
*/
- (NSString *)getTextIndex:(int32_t)index __attribute__((swift_name("getText(index:)")));

/**
 * @param index annotations androidx.annotation.IntRange(from=0.toLong())
*/
- (BOOL)isNullIndex:(int32_t)index __attribute__((swift_name("isNull(index:)")));
- (void)reset __attribute__((swift_name("reset()")));
- (BOOL)step __attribute__((swift_name("step()")));
@end


/**
 * @note annotations
 *   androidx.annotation.RestrictTo(value=[Scope.LIBRARY_GROUP_PREFIX])
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Room_runtimeRoomOpenDelegate.ValidationResult")))
@interface ComposeAppRoom_runtimeRoomOpenDelegateValidationResult : ComposeAppBase
- (instancetype)initWithIsValid:(BOOL)isValid expectedFoundMsg:(NSString * _Nullable)expectedFoundMsg __attribute__((swift_name("init(isValid:expectedFoundMsg:)"))) __attribute__((objc_designated_initializer));
@property (readonly) NSString * _Nullable expectedFoundMsg __attribute__((swift_name("expectedFoundMsg")));
@property (readonly) BOOL isValid __attribute__((swift_name("isValid")));
@end

__attribute__((swift_name("KotlinIterator")))
@protocol ComposeAppKotlinIterator
@required
- (BOOL)hasNext __attribute__((swift_name("hasNext()")));
- (id _Nullable)next __attribute__((swift_name("next()")));
@end

__attribute__((swift_name("KotlinCoroutineContextElement")))
@protocol ComposeAppKotlinCoroutineContextElement <ComposeAppKotlinCoroutineContext>
@required
@property (readonly) id<ComposeAppKotlinCoroutineContextKey> key __attribute__((swift_name("key")));
@end

__attribute__((swift_name("KotlinCoroutineContextKey")))
@protocol ComposeAppKotlinCoroutineContextKey
@required
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinByteArray")))
@interface ComposeAppKotlinByteArray : ComposeAppBase
+ (instancetype)arrayWithSize:(int32_t)size __attribute__((swift_name("init(size:)")));
+ (instancetype)arrayWithSize:(int32_t)size init:(ComposeAppByte *(^)(ComposeAppInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (int8_t)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (ComposeAppKotlinByteIterator *)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(int8_t)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end

__attribute__((swift_name("KotlinByteIterator")))
@interface ComposeAppKotlinByteIterator : ComposeAppBase <ComposeAppKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (ComposeAppByte *)next __attribute__((swift_name("next()")));
- (int8_t)nextByte __attribute__((swift_name("nextByte()")));
@end

#pragma pop_macro("_Nullable_result")
#pragma clang diagnostic pop
NS_ASSUME_NONNULL_END
